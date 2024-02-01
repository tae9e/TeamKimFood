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
    const [recommendations, setRecommendations] = useState(0); // 추천 수를 위한 상태

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
                <div key={index} className={"min-w-[100px]"}>
                    <p className={"text-lg"}>재료 : {item.ingredients}</p>
                    <p className={"text-lg"}>용량 : {item.dosage}</p>
                </div>
            ));
        }
        return null;
    };

    const renderImagesAndDescriptions = () => {
        if (recipe && recipe.oneRecipeImgVos) {
            return recipe.oneRecipeImgVos.map((img, index) => (
                <div key={index} className={"flex my-4 border-b border-gray-200 pb-4"}>
                    <img src={img.imgUrl} alt={`Recipe Image ${index}`} className={"w-48 h-48 object-cover"}/>
                    <div className="ml-4">
                        <p className="text-lg font-semibold">조리설명:</p>
                        <p className="text-lg">{img.explanation}</p>
                    </div>
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
            return <p>작성일: {formatDate(recipe.oneRecipeDto.writeDate)}</p>;
        } else if (recipe) {
            return <p>수정일: {formatDate(recipe.oneRecipeDto.correctionDate)}</p>;
        }
        return null;
    };
    const formatDate = (dateString) => {
        const options = {year: '2-digit', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
        return new Date(dateString).toLocaleDateString('ko-KR', options);
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
    const handleRecommend = async () => {
        if (!authToken) {
            alert('로그인이 필요한 기능입니다.');
            return; // 함수 실행 중단
        }

        try {
            const response = await axios.post(`http://localhost:8080/api/recipes/${id}/recommend`, null, {
                headers: {
                    'Authorization': `Bearer ${authToken}`
                }
            });
            // setRecommendations(response.data); // 서버로부터 받은 추천 수를 상태에 반영
            setRecipe(prevRecipe => ({
                ...prevRecipe,
                totalScore: response.data
            }));
        } catch (error) {
            console.error('추천 처리중 오류가 발생했습니다.', error);
            alert('로그인이 필요한 기능입니다.')
        }
    };

    //로그인 확인 후 댓글 작성
    const isLoggedIn = true;

    const saveComment = async () => {
        try {
            if (!isLoggedIn) {
                alert("로그인 후 댓글을 작성할 수 있습니다.");
                return;
            }
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
                commentDate: commentData.commentDate
            })
        } catch (error) {
            console.error("알 수 없는 오류로 댓글을 달 수 없습니다.", error);
        };
        commentData();
    }

    //댓글 수정
    const updateComment = async (commentId, updatedContent) => {
        try {
            // 로그인 확인
            if (!isLoggedIn) {
                alert("로그인 후 댓글을 수정할 수 있습니다.");
                return;
            }

            // 댓글 수정 요청
            await axios.put(`/api/comments/${commentId}`, {
                content: updatedContent
            }, {
                headers: {
                    'Authorization': `Bearer ${authToken}`
                }
            });

            // 필요한 작업 수행 (예: 수정된 댓글 화면 갱신)

        } catch (error) {
            console.error("알 수 없는 오류로 댓글을 수정할 수 없습니다.", error);
        }
    };

    //댓글 삭제
    const deleteComment = async (commentId) => {
        try {
            // 로그인 확인
            if (!isLoggedIn) {
                alert("로그인 후 댓글을 삭제할 수 있습니다.");
                return;
            }

            // 댓글 삭제 요청
            await axios.delete(`/api/comments/${commentId}`, {
                headers: {
                    'Authorization': `Bearer ${authToken}`
                }
            });

            // 필요한 작업 수행 (예: 삭제된 댓글 화면 갱신)

        } catch (error) {
            console.error("알 수 없는 오류로 댓글을 삭제할 수 없습니다.", error);
        }
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
                    <div className={"flex gap-4 flex-wrap"}>
                     {renderIngredientsAndDosages()}
                    </div>
                </div>
                <div className="border-t pt-4 mt-4">
                    {renderImagesAndDescriptions()}
                </div>
                <div>
                    {/*좌측하단*/}
                    <div className="flex justify-between border-t pt-4 mt-4">
                        <button
                            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                            type="button" onClick={navigateBackToList}>리스트 보기
                        </button>
                        {renderEditAndDeleteButtons()}
                    </div>

                </div>
                {/*추천버튼*/}
                <div className="border-t pt-4 mt-4">
                    <button type="button" onClick={handleRecommend}
                            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                        {recipe.totalScore} 추천하기
                    </button>
                </div>
                {/*댓글*/}
                <div className="setCommentForm">
                    {/* 로그인 상태에 따라 다른 메시지 표시 */}
                    <input type="textarea" placeholder={isLoggedIn ? "댓글을 작성해주세요" : "로그인 후에 댓글을 쓸 수 있습니다."}/>
                </div>
                {/* 댓글 삭제 버튼 */}
                <div className="flex justify-end mt-4">
                    <button
                        className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                        onClick={() => deleteComment(commentId)}
                    >
                        댓글 삭제
                    </button>
                </div>
            </div>
        </div>
    );
};

export default RecipeView;