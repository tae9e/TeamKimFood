import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams, useLocation } from 'react-router-dom';

const RecipeCorrection = () => {
    const [recipe, setRecipe] = useState(null);
    const navigate = useNavigate();
    const { id } = useParams();
    const authToken = localStorage.getItem('token'); // 현재 로그인한 사용자의 ID
    const location = useLocation();
    const fromPage = location.state?.fromPage || 0; // 리스트 페이지에서 전달된 페이지 번호
    const [recipeForm, setRecipeForm] = useState({
        title: '',
        content: '',
        situation: '',
        foodStuff: '',
        foodNationType: '',
        details: [{ ingredients: [''], dosage: ['']}],
        recips : [{explanations: [''], imgFiles: [null], repImageIndex: null }],
    });

    useEffect(() => {
        const loadRecipe = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/recipe/${id}`,{
                    headers : {
                        'Authorization': `Bearer ${authToken}`
                    }
                });
                setRecipe(response.data);
            } catch (error) {
                console.error('레시피를 불러오는 데 실패했습니다.', error);
            }
        };

        loadRecipe();
    }, [id, authToken]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setRecipeForm(prevForm => ({
            ...prevForm,
            [name]: value
        }));
    };

    if (!recipe) {
        return <div>Loading...</div>;
    }
    const displayDate = () => {
        if (recipe && recipe.oneRecipeDto.writeDate === recipe.oneRecipeDto.correctionDate) {
            return <p>작성일: {recipe.oneRecipeDto.writeDate}</p>;
        } else if (recipe) {
            return <p>수정일: {recipe.oneRecipeDto.correctionDate}</p>;
        }
        return null;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.put(`/api/recipes/${recipeId}`, recipeData);
            alert('Recipe updated successfully');
            navigate('/recipes');
        } catch (error) {
            console.error('Error updating recipe', error);
        }
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

    const handleAddInputPairForDetails = () => {
        setRecipeForm((prevForm) => ({
            ...prevForm,
            details: [...prevForm.details, { ingredients: [''], dosage: ['']}],
        }));
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

    return (
        <div className={'container mx-auto mt-10'}>
            <form onSubmit={handleSubmit}>
                <div className={'border p-5 rounded-lg'}>
                    <div>
                        <input type="hidden" value={recipe.oneRecipeDto.id}/>
                        <h1 className="text-2xl font-bold">"{recipe.oneRecipeDto.title}></h1>
                        <p>조회수 : {recipe.oneRecipeDto.viewCount}</p>
                        {displayDate()}
                        <p>작성자 {recipe.oneRecipeDto.nickName}</p>
                        <div>
                            <label>내용:</label>
                            <textarea
                                name="content"
                                value={recipeForm.content}
                                onChange={handleInputChange}
                            />
                        </div>
                    </div>
                    <div className={'border-t pt-4 mt-4'}>
                        <label>#음식태그</label>
                        <fieldset>
                            <legend className={'block text-gray-700 text-sm font-bold mb-2'}>상황</legend>
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
                                    checked={recipeForm.situation === 'TOGETHER'}
                                    onChange={handleInputChange}
                                />
                                같이
                            </label>
                            {/* 이야기 해보고 이름 맞춰서 넣기 */}
                        </fieldset>

                        <fieldset>
                            <legend className={'block text-gray-700 text-sm font-bold mb-2'}>주재료:</legend>
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
                                    checked={recipeForm.foodStuff === 'EGG'}
                                    onChange={handleInputChange}
                                />
                                계란
                            </label>
                            {/* 이야기 해보고 이름 맞춰서 넣기 */}
                        </fieldset>

                        <fieldset>
                            <legend className={'block text-gray-700 text-sm font-bold mb-2'}>분류:</legend>
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
                                    checked={recipeForm.foodNationType === 'CHINESE'}
                                    onChange={handleInputChange}
                                />
                                중식
                            </label>
                            <label>
                                <input
                                    type="radio"
                                    name="foodNationType"
                                    value="JAPANESE"
                                    checked={recipeForm.foodNationType === 'JAPANESE'}
                                    onChange={handleInputChange}
                                />
                                일식
                            </label>
                            <label>
                                <input
                                    type="radio"
                                    name="foodNationType"
                                    value="ETCWESTERN"
                                    checked={recipeForm.foodNationType === 'ETCWESTERN'}
                                    onChange={handleInputChange}
                                />
                                양식
                            </label>
                            <label>
                                <input
                                    type="radio"
                                    name="foodNationType"
                                    value="ETC"
                                    checked={recipeForm.foodNationType === 'ETC'}
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
                    <div className={'mb-4 border p-3 rounded-lg'}>
                        {recipeForm.recips.map((detail, pairIndex) => (
                            <fieldset key={pairIndex}>
                                <legend className={'block text-gray-700 text-sm font-bold mb-2'}>이미지 파일:</legend>
                                {detail.imgFiles.map((imgFile, index) => (
                                    <div key={index}>
                                        <img src={imgFile} alt={`Preview ${index}`}
                                             style={{maxWidth: '100px', maxHeight: '100px'}}/>


                                        <input type="file" onChange={(e) => handleImageChange(e, pairIndex, index)}/>
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
                                        <legend className={'block text-gray-700 text-sm font-bold mb-2'}>조리과정 설명:
                                        </legend>
                                        <input
                                            type="text"
                                            value={explanation}
                                            onChange={(e) => handleExplanationChange(pairIndex, index, e.target.value)}
                                            className={'shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'}/>
                                    </div>
                                ))}
                                <button type="button" onClick={() => handleAddExplanation(pairIndex)}>
                                    설명 및 이미지 추가
                                </button>

                            </fieldset>
                        ))}
                    </div>
                    <div>
                        <div>
                            <button type={"submit"}>업데이트</button>
                        </div>
                    </div>

                </div>
            </form>
        </div>
    );
};

export default RecipeCorrection;