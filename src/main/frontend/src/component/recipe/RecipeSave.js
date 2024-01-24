import React, { useState } from 'react';
import axios from 'axios';

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
    // const handleArrayInputChange = (pairIndex, type, subIndex, value) => {
    //     const newDetails = [...recipeForm.details];
    //     newDetails[pairIndex][type][subIndex] = value;
    //     setRecipeForm((prevForm) => ({
    //         ...prevForm,
    //         details: newDetails,
    //     }));
    // };
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

        // API변수 변환
        const formData = {
            memberId: 1, // 나중에 실제 로그인한 멤버 아이디 불러오기

            recipeDto: {
                title: recipeForm.title,
                content: recipeForm.content,
                foodImgDtos: recipeForm.recips.map(recip => ({
                    explanations: recip.explanations
                })),
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
            foodImgFileList: recipeForm.details.map((detail) =>
                detail.imgFiles[0]), // Fill this array with actual image files
        };

        try {
            const response = await axios.post('/api/recipes/save', formData);

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
                                    onChange={(e) => handleArrayInputChange(pairIndex, 'ingredients',index, e.target.value)}
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

            {recipeForm.recips.map((detail, pairIndex) => (
                <div key={pairIndex}>
                    <fieldset>
                        <legend>이미지 파일:</legend>
                        {detail.imgFiles.map((imgFile, index) => (
                            <div key={index}>
                                <input
                                    type="file"
                                    onChange={(e) => {
                                        handleArrayInputChange(pairIndex, 'imgFiles', index, e.target.files[0]);
                                        handleImagePreview(pairIndex, index, e.target.files[0]);
                                    }}
                                />
                                {imgFile && (
                                    <img src={imgFile} alt={`Preview ${index}`}
                                         style={{maxWidth: '100px', maxHeight: '100px'}}/>
                                )}
                            </div>
                        ))}
                    </fieldset>
                    <fieldset>
                        <legend>설명:</legend>
                        {detail.explanations.map((explanation, index) => (
                            <div key={index}>
                                <input
                                    type="text"
                                    value={explanation}
                                    onChange={(e) => handleExplanationChange(pairIndex, index, e.target.value)}
                                />
                            </div>
                        ))}
                    </fieldset>
                    <button type="button" onClick={() => handleAddExplanation(pairIndex)}>
                        설명 및 이미지 추가
                    </button>
                </div>
            ))}


            <button type="submit">레시피 저장</button>
        </form>
    );
};
export default RecipeForm;