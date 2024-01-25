import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {useParams} from "react-router-dom";

const RecipeForm = () => {
    const [recipeForm, setRecipeForm] = useState({
        title: '',
        content: '',
        situation: '',
        foodStuff: '',
        foodNationType: '',
        details: [{ ingredients: [''], dosage: ['']}],
        recips : [{explanations: [''], imgFiles: [null] }],
    });

    const {recipeId} = useParams();//recipeId 파라미터에서 가져옴
    const [newImages, setNewImages] = useState([]);
    const [isEditMode, setIsEditMode] = useState(false);

    useEffect(() => {
        if (recipeId) {
            setIsEditMode(true); // URL에 recipeId가 있으면 수정 모드로 설정
            const RecipeData = async () => {
                const authToken = localStorage.getItem('변수명합의보기');
                try {
                    const response = await axios.get(`/api/recipes/${recipeId}`, {
                        headers: {
                            'Authorization': `Bearer ${authToken}`
                        }
                    });
                    const recipeData = response.data;
                    setRecipeForm({
                        title: recipeData.title,
                        content: recipeData.content,
                        situation: recipeData.situation,
                        foodStuff: recipeData.foodStuff,
                        foodNationType: recipeData.foodNationType,
                        details: recipeData.details.map(detail => ({
                            ingredients: detail.ingredients,
                            dosage: detail.dosage
                        })),
                        recips: recipeData.recips.map(recip => ({
                            explanations: recip.explanations,
                            imgFiles: recip.imgFiles.map(imgFile => imgFile.imgUrl) // imgUrl을 사용
                        }))
                    });
                } catch (error) {
                    console.error("레시피 불러오기 실패", error);
                }
            };
            RecipeData();
        }
    }, [recipeId]);
    const handleNewImageChange = (e, pairIndex) => {
        // 새 이미지 파일을 newImages 상태에 추가
        const files = Array.from(e.target.files);
        setNewImages((prev) => {
            const updated = [...prev];
            updated[pairIndex] = files;
            return updated;
        });
    };
    const handleImageChange = (e, pairIndex, index) => {
        // 이미지 파일 처리 및 미리보기 업데이트
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (readEvent) => {
                setRecipeForm(prevForm => {
                    const updatedRecips = [...prevForm.recips];
                    updatedRecips[pairIndex].imgFiles[index] = readEvent.target.result;
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
        setRecipeForm((prevForm) => {
            const newRecips = [...prevForm.recips, { explanations: [''], imgFiles: [null] }];
            return {
                ...prevForm,
                recips: newRecips,
            };
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        //const email = localStorage.get???('email'); 이 변수 값을 memberid에 넣을 예정 로그인 후 사용자 로컬저장할 변수명을 ?에 쓸 예정 get???
        // API변수 변환
        const formData = new FormData();
        formData.append('recipeRequest', JSON.stringify({
            memberId: 1, // 실제 로그인한 멤버 아이디로 대체해야함
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
                ingredients: detail.ingredients,
                dosage: detail.dosage,
            })),
            explanations: recipeForm.recips.map((recip) => recip.explanations)
        }));
        recipeForm.recips.forEach((recip, index) => {
            if (recip.imgFiles[0]) {
                formData.append(`foodImgFileList`, recip.imgFiles[0]);
            }
        });

        try {
            const response = await axios.post('/api/recipes/save', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });

            if (response.status === 200) {
                const data = response.data;
                console.log('레시피를 성공적으로 저장했습니다.:', data);
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
            recipeDetailListDto: recipeForm.details.map((detail) => ({
                ingredients: detail.ingredients,
                dosage: detail.dosage,
            })),
            explanations: recipeForm.recips.map((recip) => recip.explanations)
        }));
        //이미지 파일 추가
        newImages.forEach((files, index) => {
            files.forEach((file, fileIndex) => {
                formData.append(`newFoodImgFileList[${index}][${fileIndex}]`, file);
            });

        });
        try {
            const response = await axios.put(`/api/recipes/${recipeId}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });

            if (response.status === 200) {
                console.log('레시피가 성공적으로 수정되었습니다.');
                // 성공 후 처리 로직 (예: 페이지 이동)
            } else {
                console.error('레시피 수정에 실패했습니다.');
            }
        } catch (error) {
            console.error('에러가 발생했습니다:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>제목</label>
            <input type="text" name="title" value={recipeForm.title} onChange={handleInputChange}/>

            <label>음식소개</label>
            <textarea name="content" value={recipeForm.content} onChange={handleInputChange}/>

            <label>태그</label>
            <fieldset>
                <legend>상황</legend>
                <label>
                    <input
                        type="radio"
                        name="situation"
                        value="ALONE"
                        checked={recipeForm.situation === 'ALONE'}
                        onChange={handleInputChange}
                    />
                    혼자
                </label>
                <label>
                    <input
                        type="radio"
                        name="situation"
                        value="TOGETHER"
                        onChange={handleInputChange}
                    />
                    같이
                </label>
                {/* 이야기 해보고 이름 맞춰서 넣기 */}
            </fieldset>

            <fieldset>
                <legend>주재료:</legend>
                <label>
                    <input
                        type="radio"
                        name="foodStuff"
                        value="MEAT"
                        checked={recipeForm.foodStuff === 'MEAT'}
                        onChange={handleInputChange}
                    />
                    고기
                </label>
                <label>
                    <input
                        type="radio"
                        name="foodStuff"
                        value="EGG"
                        onChange={handleInputChange}
                    />
                    계란
                </label>
                {/* 이야기 해보고 이름 맞춰서 넣기 */}
            </fieldset>

            <fieldset>
                <legend>분류:</legend>
                <label>
                    <input
                        type="radio"
                        name="foodNationType"
                        value="KOREAN"
                        checked={recipeForm.foodNationType === 'KOREAN'}
                        onChange={handleInputChange}
                    />
                    한식
                </label>
                <label>
                    <input
                        type="radio"
                        name="foodNationType"
                        value="CHINESE"
                        onChange={handleInputChange}
                    />
                    중식
                </label>
                <label>
                    <input
                        type="radio"
                        name="foodNationType"
                        value="JAPANESE"
                        onChange={handleInputChange}
                    />
                    일식
                </label>
                <label>
                    <input
                        type="radio"
                        name="foodNationType"
                        value="ETCWESTERN"
                        onChange={handleInputChange}
                    />
                    양식
                </label>
                <label>
                    <input
                        type="radio"
                        name="foodNationType"
                        value="ETC"
                        onChange={handleInputChange}
                    />
                    기타
                </label>

            </fieldset>
            {recipeForm.details.map((detail, pairIndex) => (
                <div key={pairIndex}>
                    <fieldset>
                        <legend>재료:</legend>
                        {detail.ingredients.map((ingredient, index) => (
                            <div key={index}>
                                <input
                                    type="text"
                                    value={ingredient}
                                    onChange={(e) => handleArrayInputChange(pairIndex, 'ingredients', index, e.target.value)}
                                />
                            </div>
                        ))}
                    </fieldset>

                    <fieldset>
                        <legend>용량:</legend>
                        {detail.dosage.map((dose, index) => (
                            <div key={index}>
                                <input
                                    type="text"
                                    value={dose}
                                    onChange={(e) => handleArrayInputChange(pairIndex, 'dosage', index, e.target.value)}
                                />
                            </div>
                        ))}
                    </fieldset>
                </div>
            ))}

            <button type="button" onClick={handleAddInputPairForDetails}>
                재료 및 용량 추가
            </button>

            {/* 이미지 및 설명 필드 */}
            {recipeForm.recips.map((detail, pairIndex) => (
                <fieldset key={pairIndex}>
                    <legend>이미지 파일:</legend>
                    {detail.imgFiles.map((imgFile, index) => (
                        <div key={index}>
                            <img src={imgFile} alt={`Preview ${index}`} style={{ maxWidth: '100px', maxHeight: '100px' }} />
                            {isEditMode && (
                                <input type="file" onChange={(e) => handleImageChange(e, pairIndex, index)} />
                            )}
                        </div>
                    ))}
                    {isEditMode && (
                        <input type="file" multiple onChange={(e) => handleNewImageChange(e, pairIndex)} />
                    )}
                    {detail.explanations.map((explanation, index) => (
                        <div key={index}>
                            <input
                                type="text"
                                value={explanation}
                                onChange={(e) => handleExplanationChange(pairIndex, index, e.target.value)}
                            />
                        </div>
                    ))}
                    <button type="button" onClick={() => handleAddExplanation(pairIndex)}>
                        설명 및 이미지 추가
                    </button>
                </fieldset>
            ))}
            {/* 버튼 표시 조건: 수정 모드일 경우 '레시피 수정', 그렇지 않으면 '레시피 저장' */}
            {isEditMode ? (
                <button type="button" onClick={handleUpdate}>레시피 수정</button>
            ) : (
                <button type="submit">레시피 저장</button>
            )}
        </form>
    );
};
export default RecipeForm;