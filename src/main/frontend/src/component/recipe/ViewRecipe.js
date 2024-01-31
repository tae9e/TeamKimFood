import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams, useLocation } from 'react-router-dom';

const RecipeView = () => {
    const [recipe, setRecipe] = useState(null);
    const navigate = useNavigate();
    const { id } = useParams();
    const authToken = localStorage.getItem('token'); // 현재 로그인한 사용자의 ID
    const location = useLocation();
    const fromPage = location.state?.fromPage || 0; // 리스트 페이지에서 전달된 페이지 번호

    useEffect(() => {
        const loadRecipe = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/recipe/${id}`,{
                    headers: {
                        'Authorization': `Bearer ${authToken}`
                    }
                });
                setRecipe(response.data);
            } catch (error) {
                console.error('레시피를 불러오는 데 실패했습니다.', error);
            }
        };

        loadRecipe();
    }, [id]);
    const renderIngredientsAndDosages = () => {
        if (recipe && recipe.oneRecipeIngDoVos) {
            return recipe.oneRecipeIngDoVos.map((item, index) => (
                <div key={index}>
                    <p>재료 : {item.ingredients}</p>
                    <p>용량 : {item.dosage}</p>
                </div>
            ));
        }
        return null;
    };

    const renderImagesAndDescriptions = () => {
        if (recipe && recipe.oneRecipeImgVos) {
            return recipe.oneRecipeImgVos.map((img, index) => (
                <div key={index}>
                    <img src={img.imgUrl} alt={`Recipe Image ${index}`} />
                    조리설명 : {img.explanation}
                </div>
            ));
        }
        return null;
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

    const isAuthor = recipe.equalMember; // 현재 사용자가 레시피 작성자인지 확인

    // 수정 및 삭제 버튼 렌더링 함수
    const renderEditAndDeleteButtons = () => {
        if (isAuthor) {
            return (
                <>
                    <button onClick={() => navigate(`/api/recipe/${id}/update`)}
                    className={'bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline'}>수정</button>
                    <button onClick={handleDelete}
                    className={'bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline'}>삭제</button>
                </>
            );
        }
        return null;
    };

    // 삭제 처리 함수
    const handleDelete = async () => {
        if (window.confirm('레시피를 삭제하시겠습니까?')) {
            try {
                await axios.delete(`http://localhost:8080/api/recipes/${id}/delete`,{
                    headers: {
                        'Authorization': `Bearer ${authToken}`
                    }
                });
                alert('레시피가 삭제되었습니다.');
                navigate(`/main?page=${fromPage}`);
            } catch (error) {
                console.error('레시피 삭제 중 오류 발생', error);
            }
        }
    };
    const navigateBackToList = () => {
        navigate(`/main?page=${fromPage}`);
    };


    if (!recipe) {
        return <div>Loading...</div>;
    }
    return (
        <div className={'container mx-auto mt-10'}>
            <div className={'border p-5 rounded-lg'}>
                <div>
                    <input type="hidden" value={recipe.oneRecipeDto.id}/>
                    <h1 className="text-2xl font-bold">{recipe.oneRecipeDto.title}</h1>
                    <p>조회수 : {recipe.oneRecipeDto.viewCount}</p>
                    {displayDate()}
                    <p>작성자 {recipe.oneRecipeDto.nickName}</p>
                    <p>{recipe.oneRecipeDto.content}</p>
                </div>
                <div className={'border-t pt-4 mt-4'}>
                    <p>#태그</p>
                    <p>상황 : {recipe.oneRecipeDto.situation}</p>
                    <p>주재료 : {recipe.oneRecipeDto.foodStuff}</p>
                    <p>국가별 : {recipe.oneRecipeDto.foodNationType}</p>
                </div>
                <div className="border-t pt-4 mt-4">
                    {renderIngredientsAndDosages()}
                </div>
                <div className="border-t pt-4 mt-4">
                    {renderImagesAndDescriptions()}
                </div>
                <div>
                    {/*좌측하단*/}
                    <div className="flex justify-between border-t pt-4 mt-4">
                        <button
                            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                            type="button" onClick={navigateBackToList}>리스트 보기</button>
                        {renderEditAndDeleteButtons()}
                    </div>

                </div>
                {/*추천버튼*/}
                <div className="border-t pt-4 mt-4">
                    추천버튼
                </div>
                {/*댓글*/}
                <div>

                </div>
            </div>
        </div>
    );
};

export default RecipeView;