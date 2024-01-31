import React, {useState, useEffect} from "react";
import axios from "axios";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../Css/Common.css';
import {useNavigate} from 'react-router-dom';

const RecipeView = ({ match }) => {
    const [recipe, setRecipe] = useState(null);
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const [recommendations, setRecommendations] = useState(0);

    //추천버튼
    const renderRecommendButton = () => (
        <button onClick={handleRecommend}>{`추천 (${recommendations})`}</button>
    );

    // 레시피 상세 정보를 로드하는 함수
    const loadRecipe = async () => {
        try {
            const response = await axios.get(`/api/recipes/${match.params.id}`);
            setRecipe(response.data);
        } catch (error) {
            setError('레시피를 불러오는 데 실패했습니다.');
        }
    };

    // 컴포넌트 마운트 시 레시피 로드
    useEffect(() => {
        loadRecipe();
    }, [match.params.id]);

    if (error) {
        return <div>{error}</div>;
    }

    if (!recipe) {
        return <div>Loading...</div>;
    }
    const displayDate = () => {
        if (recipe.oneRecipeDto.writeDate === recipe.oneRecipeDto.correctionDate) {
            return <p>작성일: {recipe.oneRecipeDto.writeDate}</p>;
        } else {
            return <p>수정일: {recipe.oneRecipeDto.correctionDate}</p>;
        }
    };
    // 이미지 URL 및 설명을 렌더링하는 함수
    const renderImagesAndDescriptions = () => {
        return recipe.oneRecipeDto.imgUrls.map((url, index) => (
            <div key={index}>
                <img src={url} alt={`Recipe Image ${index}`} />
                <p>{recipe.oneRecipeDto.explanations[index]}</p>
            </div>
        ));
    };
    // 재료와 용량을 렌더링하는 함수
    const renderIngredientsAndDosages = () => {
        return recipe.oneRecipeDto.ingredients.map((ingredient, index) => (
            <div key={index}>
                <p>재료: {ingredient}</p>
                <p>용량: {recipe.oneRecipeDto.dosage[index]}</p>
            </div>
        ));
    };
    const navigateToMain=()=>{
        navigate("recipeList");
    }
    // 현재 로그인한 사용자의 ID (실제 프로젝트에서는 인증 시스템을 통해 얻어야 합니다)
    // 나중에 로그인 구체화시 저장할 값 합의하기
    const loggedInUserId = localStorage.getItem('변수명');

    // 수정 및 삭제 버튼을 렌더링하는 함수
    const renderEditAndDeleteButtons = () => {
        if (recipe.oneRecipeDto.memberId === loggedInUserId) {
            return (
                <div className="flex space-x-2 mt-2">
                    <button type="button" onClick={() => navigateToEditPage(recipe.oneRecipeDto.id)}
                            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                    >수정</button>
                    <button type="button" onClick={() => handleDelete(recipe.oneRecipeDto.id)}
                            className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                    >삭제</button>
                </div>
            );
        }
        return null;
    };

    // 수정 페이지로 이동하는 함수
    const navigateToEditPage = (recipeId) => {
        navigate(`/editRecipe/${recipeId}`);
    };

    // 레시피 삭제 처리 함수
    const handleDelete = async (recipeId) => {
        // 삭제 확인
        if (window.confirm('레시피를 삭제하시겠습니까?')) {
            try {
                // 삭제 API 호출
                await axios.delete(`/api/recipes/${recipeId}`);
                alert('레시피가 삭제되었습니다.');
                // 삭제 후 목록 페이지로 이동
                navigate('/recipeList');
            } catch (error) {
                console.error('레시피 삭제 중 오류 발생', error);
                alert('레시피 삭제 중 문제가 발생했습니다.');
            }
        }
    };
    //추천버튼 로직
    const handleRecommend = async () => {
        try {
            const response = await axios.post(`/api/recipes/${recipe.oneRecipeDto.id}/recommend`);
            setRecommendations(response.data); // 추천 수 업데이트
        } catch (error) {
            console.error('추천 처리 중 오류 발생', error);
            alert('추천 처리 중 문제가 발생했습니다.');
        }
    };

    //댓글쓸 때 로그인 여부 확인


    //로그인 확인 후 리다이렉트? 댓글 작성
    const saveComment = async () => {
        try {
            const response = await axios.get(`/api/recipes/comments/${commentId}`, {
                headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });
            const commentData = response.data;
            setCommentForm({
                content: commentData.content,
                nickname: commentData.nickname,
                recipeId: commentData.recipeId,
                commentDate: commentData.commentDate,

            })
    } catch(error) {

        }


    const RecipeData = async () => {
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

    //댓글 수정


    //댓글 삭제



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
                    <p>{recipe.oneRecipeDto.situation}</p>
                    <p>{recipe.oneRecipeDto.foodStuff}</p>
                    <p>{recipe.oneRecipeDto.foodNationType}</p>
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
                            type="button" onClick={() => navigateToMain()}>메인</button>
                    </div>
                    {/*우측하단*/}
                    <div>
                        {renderEditAndDeleteButtons()}
                    </div>
                </div>
                {/*추천버튼*/}
                <div className="border-t pt-4 mt-4">
                    {renderRecommendButton()}
                </div>
                {/*댓글*/}
                <div classname="setCommentForm">
                    <input type="textarea" placeholder="로그인 후에 댓글을 쓸 수 있습니다." />

                </div>
            </div>
        </div>
    );
};

export default RecipeView;