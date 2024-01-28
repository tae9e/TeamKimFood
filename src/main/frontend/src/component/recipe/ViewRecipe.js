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
                <div>
                    <button type="button" onClick={() => navigateToEditPage(recipe.oneRecipeDto.id)}>수정</button>
                    <button type="button" onClick={() => handleDelete(recipe.oneRecipeDto.id)}>삭제</button>
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

    return (
        <div>
            <div>
                <div>
                    <input type="hidden" value={recipe.oneRecipeDto.id}/>
                    <h1>{recipe.oneRecipeDto.title}</h1>
                    <p>조회수 : {recipe.oneRecipeDto.viewCount}</p>
                    {displayDate()}
                    <p>작성자 {recipe.oneRecipeDto.nickName}</p>
                    <p>{recipe.oneRecipeDto.content}</p>
                </div>
                <div>
                    <p>{recipe.oneRecipeDto.situation}</p>
                    <p>{recipe.oneRecipeDto.foodStuff}</p>
                    <p>{recipe.oneRecipeDto.foodNationType}</p>
                </div>
                <div>
                    {renderIngredientsAndDosages()}
                </div>
                <div>
                    {renderImagesAndDescriptions()}
                </div>
                <div>
                    {/*좌측하단*/}
                    <div>
                        <button type="button" onClick={() => navigateToMain()}>메인</button>
                    </div>
                    {/*우측하단*/}
                    <div>
                        {renderEditAndDeleteButtons()}
                    </div>
                </div>
                {/*추천버튼*/}
                <div>
                    {renderRecommendButton()}
                </div>
                {/*댓글*/}
                <div>

                </div>
            </div>
        </div>
    );
};

export default RecipeView;