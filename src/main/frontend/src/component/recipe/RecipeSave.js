import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {useParams, useNavigate} from "react-router-dom";

const RecipeForm = () => {
    const [recipeForm, setRecipeForm] = useState({
        title: '',
        content: '',
        situation: '',
        foodStuff: '',
        foodNationType: '',
        details: [{ ingredients: [''], dosage: ['']}],
        recips : [{explanations: [''], imgFiles: [null], repImageIndex: null }],
    });

    const {id} = useParams();//recipeId 파라미터에서 가져옴
    const [repImageIndex, setRepImageIndex] = useState(null);
    const [newImages, setNewImages] = useState([]);
    const [isEditMode, setIsEditMode] = useState(false);
    const navigate = useNavigate();
    const authToken = localStorage.getItem('token');
    const [recipe, setRecipe] = useState(null);
    // 기존 이미지를 추적하기 위한 새로운 state 추가
    const [existingImages, setExistingImages] = useState([]);
    useEffect(() => {
        if (id) {
            setIsEditMode(true);
            // 기존 레시피 데이터 불러오기
            axios.get(`http://localhost:8080/api/recipes/${id}`, { headers: { 'Authorization': `Bearer ${authToken}` }})
                .then(response => {
                    const recipeData = response.data.oneRecipeDto;
                    const imgData = response.data.oneRecipeImgVos;
                    const ingDoData = response.data.oneRecipeIngDoVos;

                    setRecipeForm({
                        title: recipeData.title,
                        content: recipeData.content,
                        situation: recipeData.situation,
                        foodStuff: recipeData.foodStuff,
                        foodNationType: recipeData.foodNationType,
                        details: ingDoData.map(item => ({ ingredients: [item.ingredients], dosage: [item.dosage] })),
                        recips: imgData.map(item => ({ explanations: [item.explanation], imgFiles: [{dataUrl:item.imgUrl}] })),
                    });
                    setExistingImages(imgData.map(item => ({ imgUrl: item.imgUrl, explanation: item.explanation })));
                })
                .catch(error => console.error("레시피 불러오기 실패", error));
        }
    }, [id, authToken]);
    const handleNewImageChange = (e, pairIndex) => {
        // 새 이미지 파일을 newImages 상태에 추가
        const files = Array.from(e.target.files);
        setNewImages((prev) => {
            const updated = [...prev];
            updated[pairIndex] = files.map(file => ({ index: pairIndex, file: file }));
            return updated;
        });
    };
    const handleImageChange = (e, pairIndex, index) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                setRecipeForm(prevForm => {
                    const updatedRecips = [...prevForm.recips];
                    const updatedImgFiles = [...updatedRecips[pairIndex].imgFiles];

                    // 파일 데이터와 원래 이름을 저장
                    updatedImgFiles[index] = {
                        dataUrl: reader.result,
                        originalName: file.name
                    };

                    updatedRecips[pairIndex].imgFiles = updatedImgFiles;

                    return {...prevForm, recips: updatedRecips};
                });
            };
            reader.readAsDataURL(file);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setRecipeForm((prevForm) => ({
            ...prevForm,
            [name]: value,
        }));
    };
    const handleArrayInputChange = (pairIndex, type, subIndex, value) => {
        setRecipeForm((prevForm) => {
            const newDetails = [...prevForm.details];
            if (!newDetails[pairIndex]) {
                // 배열이 없는 경우, 빈 배열로 초기화
                newDetails[pairIndex] = { ingredients: [''], dosage: [''] };
            }
            if (!newDetails[pairIndex][type]) {
                // 해당 타입의 배열이 없는 경우, 빈 배열로 초기화
                newDetails[pairIndex][type] = [''];
            }
            newDetails[pairIndex][type][subIndex] = value;

            return {
                ...prevForm,
                details: newDetails,
            };
        });
    };
    const handleImagePreview = (pairIndex, index, file) => {
        const newRecips = [...recipeForm.recips];
        const reader = new FileReader();

        reader.onload = (e) => {
            newRecips[pairIndex].imgFiles[index] = e.target.result;
            setRecipeForm((prevForm) => ({
                ...prevForm,
                recips: newRecips,
            }));
        };

        reader.readAsDataURL(file);
    };

    const handleAddInputPairForDetails = () => {
        setRecipeForm((prevForm) => ({
            ...prevForm,
            details: [...prevForm.details, { ingredients: [''], dosage: ['']}],
        }));
    };
    const handleExplanationChange = (pairIndex, subIndex, value) => {
        setRecipeForm((prevForm) => {
            const newRecips = [...prevForm.recips];
            if (!newRecips[pairIndex]) {
                newRecips[pairIndex] = { explanations: [''], imgFiles: [null] };
            }
            if (!newRecips[pairIndex].explanations) {
                newRecips[pairIndex].explanations = [''];
            }
            newRecips[pairIndex].explanations[subIndex] = value;

            return {
                ...prevForm,
                recips: newRecips,
            };
        });
    };
    const handleAddExplanation = (pairIndex) => {
        setRecipeForm(prevForm => {
            const newRecips = [...prevForm.recips];
            const newRecip = {
                explanations: [''],
                imgFiles: [null],
                // repImageIndex: newRecips[pairIndex].repImageIndex // 이전 repImageIndex 값을 유지
            };

            newRecips.push(newRecip);
            return {...prevForm, recips: newRecips};//
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        // 기존 이미지를 formData에 추가
        existingImages.forEach((img, index) => {
            formData.append(`existingImgUrlList`, img.imgUrl);
            formData.append(`existingExplanations`, img.explanation);
        });

        // API변수 변환
        const formData = new FormData();
        formData.append('recipeRequest', JSON.stringify({
            recipeDto: {
                title: recipeForm.title,
                content: recipeForm.content,
            },
            categoryPreferenceDto: {
                situation: recipeForm.situation,
                foodStuff: recipeForm.foodStuff,
                foodNationType: recipeForm.foodNationType,
            },
            recipeDetailListDto: recipeForm.details.map((detail) => ({
                ingredients: detail.ingredients[0],
                dosage: detail.dosage[0],
            })),
            explanations: recipeForm.recips.map((recip) => recip.explanations[0])
        }));
        recipeForm.recips.forEach((recip, index) => {

            if (recip.imgFiles[0]) {
                const fileData = recip.imgFiles[0];//Base64 문자열
                // console.log("원래이름: ",fileData.originalName)
                const file = dataURLtoFile(fileData.dataUrl, fileData.originalName); // 저장된 원래 파일명을 사용
                formData.append(`foodImgFileList`, file);
            }
        });
        function dataURLtoFile(dataurl, filename) {
            let arr = dataurl.split(','),
                mime = arr[0].match(/:(.*?);/)[1],
                bstr = atob(arr[1]),
                n = bstr.length,
                u8arr = new Uint8Array(n);

            while (n--) {
                u8arr[n] = bstr.charCodeAt(n);
            }

            return new File([u8arr], filename, { type: mime });
        }
        const repIndex = repImageIndex ? repImageIndex.pairIndex : null;
        console.log("repImageIndex:", repIndex); // 디버깅을 위한 로그 출력
        formData.append('repImageIndex', repIndex);

        for (let [key, value] of formData.entries()) {
            console.log(key, value);
        }


        try {
            const response = await axios.post('/api/recipe/save', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': `Bearer ${authToken}`,
                },
            });

            if (response.status === 200) {
                const data = response.data;
                console.log('레시피를 성공적으로 저장했습니다.:', data);
                navigate(`/`);
            } else {
                console.error('레시피를 저장하는데 실패했습니다.:', response.statusText);
            }
        } catch (error) {
            console.error('에러가 발생했습니다.:', error);
        }
    };
    const handleUpdate = async ()=>{
        const formData = new FormData();
        formData.append('recipeRequest', JSON.stringify({
            recipeDto: {
                title: recipeForm.title,
                content: recipeForm.content,
            },
            categoryPreferenceDto: {
                situation: recipeForm.situation,
                foodStuff: recipeForm.foodStuff,
                foodNationType: recipeForm.foodNationType,
            },
            recipeDetailListDto: recipeForm.details.map(detail => ({
                ingredients: detail.ingredients[0],
                dosage: detail.dosage[0],
            })),
            explanations: recipeForm.recips.map((recip) => recip.explanations[0])
        }));
        // 기존 이미지를 formData에 추가
        existingImages.forEach((img, index) => {
            formData.append(`existingImgUrlList`, img.imgUrl);
            formData.append(`existingExplanations`, img.explanation);
        });
        //이미지 파일 추가
        if (newImages && newImages.length > 0) {
            newImages.forEach((files) => {
                files.forEach((fileObj) => {
                    if (fileObj && fileObj.file) {
                        formData.append('foodImgFileList', fileObj.file);
                    }
                });
            });
        }
        const repIndex = repImageIndex ? repImageIndex.pairIndex : null;
        console.log("repImageIndex:", repIndex); // 디버깅을 위한 로그 출력
        formData.append('repImageIndex', repIndex);
        for (let [key, value] of formData.entries()) {
            console.log(`${key}:`, value);
        }
        try {
            const response = await axios.put(`http://localhost:8080/api/recipes/${id}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': `Bearer ${authToken}`
                },
            });

            if (response.status === 200) {
                console.log('레시피가 성공적으로 수정되었습니다.');
                navigate(`/api/recipe/${id}`);
            } else {
                console.error('레시피 수정에 실패했습니다.');
            }
        } catch (error) {
            console.error('에러가 발생했습니다:', error);
        }
    };
    const handleRepImageChange = (pairIndex, index) => {
        //console.log("Selected pairIndex:", pairIndex, "Selected index:", index); // 디버깅을 위한 로그 출력
        // 이미 선택된 대표 이미지가 있는지 확인
        if (repImageIndex !== null && repImageIndex.pairIndex !== pairIndex) {
            alert("이미 다른 항목에서 대표사진을 지정하셨습니다.");
            return;
        }

        // 새로운 대표 이미지 설정 또는 해제
        if (repImageIndex && repImageIndex.pairIndex === pairIndex && repImageIndex.index === index) {
            // 이미 선택된 이미지를 다시 클릭한 경우, 선택 해제
            setRepImageIndex(null);
        } else {
            // 새로운 이미지를 선택한 경우
            setRepImageIndex({ pairIndex, index });
        }
    };

    return (
        <form onSubmit={handleSubmit} className={'max-w-lg mx-auto p-4 border border-gray-200 shadow-lg mt-10'}>
            <div className={'mb-4 border p-3 rounded-lg'}>
                <label className={'block text-gray-700 text-sm font-bold mb-2'}
                >제목</label>
                <input type="text" name="title" value={recipeForm.title} placeholder={'내 레시피를 소개할 멋진 이름을 써주세요!'} onChange={handleInputChange}
                       className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
            </div>
            <div className={'mb-4 border p-3 rounded-lg'}>
                <label className={'block text-gray-700 text-sm font-bold mb-2'}
                >음식소개</label>
                <textarea name="content" value={recipeForm.content} onChange={handleInputChange}
                          placeholder={'내 레시피가 어떤 아이인지 소개 해주세요!'}
                          className={'shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'}/>
            </div>
            <div className={'mb-4 border p-3 rounded-lg'}>
                <label>#음식태그</label>
                <fieldset>
                    <legend className={'block text-gray-700 text-sm font-bold mb-2'}>상황</legend>
                    <label>
                        <input
                            type="radio"
                            name="situation"
                            value="혼자"
                            checked={recipeForm.situation === '혼자'}
                            onChange={handleInputChange}
                        />
                        혼자
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="situation"
                            value="가족"
                            checked={recipeForm.situation === '가족'}
                            onChange={handleInputChange}
                        />
                        가족
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="situation"
                            value="친구"
                            checked={recipeForm.situation === '친구'}
                            onChange={handleInputChange}
                        />
                        친구
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="situation"
                            value="연인"
                            checked={recipeForm.situation === '연인'}
                            onChange={handleInputChange}
                        />
                        연인
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="situation"
                            value="일상"
                            checked={recipeForm.situation === '일상'}
                            onChange={handleInputChange}
                        />
                        일상
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="situation"
                            value="상차림"
                            checked={recipeForm.situation === '상차림'}
                            onChange={handleInputChange}
                        />
                        상차림
                    </label>
                    {/* 이야기 해보고 이름 맞춰서 넣기 */}
                </fieldset>

                <fieldset>
                    <legend className={'block text-gray-700 text-sm font-bold mb-2'}>주재료:</legend>
                    <label>
                        <input
                            type="radio"
                            name="foodStuff"
                            value="고기"
                            checked={recipeForm.foodStuff === '고기'}
                            onChange={handleInputChange}
                        />
                        고기
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="foodStuff"
                            value="해산물"
                            checked={recipeForm.foodStuff === '해산물'}
                            onChange={handleInputChange}
                        />
                        해산물
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="foodStuff"
                            value="채소"
                            checked={recipeForm.foodStuff === '채소'}
                            onChange={handleInputChange}
                        />
                        채소
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="foodStuff"
                            value="과일"
                            checked={recipeForm.foodStuff === '과일'}
                            onChange={handleInputChange}
                        />
                        과일
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="foodStuff"
                            value="디저트"
                            checked={recipeForm.foodStuff === '디저트'}
                            onChange={handleInputChange}
                        />
                        디저트
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="foodStuff"
                            value="음료"
                            checked={recipeForm.foodStuff === '음료'}
                            onChange={handleInputChange}
                        />
                        음료
                    </label>
                    {/* 이야기 해보고 이름 맞춰서 넣기 */}
                </fieldset>

                <fieldset>
                    <legend className={'block text-gray-700 text-sm font-bold mb-2'}>타입:</legend>
                    <label>
                        <input
                            type="radio"
                            name="foodNationType"
                            value="한식"
                            checked={recipeForm.foodNationType === '한식'}
                            onChange={handleInputChange}
                        />
                        한식
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="foodNationType"
                            value="중식"
                            checked={recipeForm.foodNationType === '중식'}
                            onChange={handleInputChange}
                        />
                        중식
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="foodNationType"
                            value="일식"
                            checked={recipeForm.foodNationType === '일식'}
                            onChange={handleInputChange}
                        />
                        일식
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="foodNationType"
                            value="양식"
                            checked={recipeForm.foodNationType === '양식'}
                            onChange={handleInputChange}
                        />
                        양식
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="foodNationType"
                            value="기타"
                            checked={recipeForm.foodNationType === '기타'}
                            onChange={handleInputChange}
                        />
                        기타
                    </label>

                </fieldset>
            </div>
            <div className={'mb-4 border p-3 rounded-lg'}>
                {recipeForm.details.map((detail, pairIndex) => (
                    <div key={pairIndex}>
                        <fieldset>
                            <legend className={'block text-gray-700 text-sm font-bold mb-2'}>재료:</legend>
                            {detail.ingredients.map((ingredient, index) => (
                                <div key={index}>
                                    <input
                                        type="text"
                                        value={ingredient}
                                        placeholder={'ex) 참기름'}
                                        onChange={(e) => handleArrayInputChange(pairIndex, 'ingredients', index, e.target.value)}
                                        className={'shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'}/>
                                </div>
                            ))}
                        </fieldset>

                        <fieldset>
                            <legend className={'block text-gray-700 text-sm font-bold mb-2'}>용량:</legend>
                            {detail.dosage.map((dose, index) => (
                                <div key={index}>
                                    <input
                                        type="text"
                                        placeholder={'ex) 1 밥숫가락 혹은 티스푼'}
                                        value={dose}
                                        onChange={(e) => handleArrayInputChange(pairIndex, 'dosage', index, e.target.value)}
                                        className={'shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'}/>
                                </div>
                            ))}
                        </fieldset>
                    </div>
                ))}
            </div>
            <button type="button" onClick={handleAddInputPairForDetails}>
                재료 및 용량 추가
            </button>

            {/* 이미지 및 설명 필드 */}
            <div className={'mb-4 border p-3 rounded-lg'}>
                {recipeForm.recips.map((detail, pairIndex) => (
                    <fieldset key={pairIndex}>
                        <legend className={'block text-gray-700 text-sm font-bold mb-2'}>이미지 파일:</legend>
                        {detail.imgFiles.map((imgFile, index) => (
                            <div key={index}>
                                <img src={imgFile && imgFile.dataUrl ? imgFile.dataUrl : ''} alt={`Preview ${index}`}
                                     style={{maxWidth: '200px', maxHeight: '200px'}}/>


                                {!isEditMode &&(<input type="file" onChange={(e) => handleImageChange(e, pairIndex, index)}/>)}
                                <input
                                    type="checkbox"
                                    checked={repImageIndex && repImageIndex.pairIndex === pairIndex && repImageIndex.index === index}
                                    onChange={() => handleRepImageChange(pairIndex, index)}
                                />대표사진 지정하기
                            </div>
                        ))}
                        {isEditMode && (

                                <input type="file" multiple onChange={(e) => handleNewImageChange(e, pairIndex)}/>
                        )}
                        {detail.explanations.map((explanation, index) => (
                            <div key={index}>
                            <legend className={'block text-gray-700 text-sm font-bold mb-2'}>조리과정 설명:</legend>
                                <input
                                    type="text"
                                    value={explanation}
                                    onChange={(e) => handleExplanationChange(pairIndex, index, e.target.value)}
                                    className={'shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'}/>
                            </div>
                        ))}
                    </fieldset>
                ))}
                        <button type="button" onClick={() => handleAddExplanation(pairIndex)}>
                            설명 및 이미지 추가
                        </button>


            </div>

            {/* 버튼 표시 조건: 수정 모드일 경우 '레시피 수정', 그렇지 않으면 '레시피 저장' */}
            <div className={'mt-4'}>
                {isEditMode ? (
                    <button type="button" onClick={handleUpdate}
                            className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                    >레시피 수정</button>
                ) : (
                    <button type="submit"
                            className="bg-teal-500 hover:bg-teal-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                    >레시피 저장</button>
                )}
            </div>
        </form>
    );
};
export default RecipeForm;