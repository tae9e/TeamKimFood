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
    
    const [comments, setComments] = useState([]);
    const [commentInput, setCommentInput] = useState('');
    const [selectedComment, setSelectedComment] = useState(null);

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

    // useEffect(() => {
    //     const loadComments = async () => {
    //         try {
    //             const response = await axios.get(`http://localhost:8080/comments`);
    //             setComments(response.data);
    //         } catch (error) {
    //             console.error('댓글을 불러오는 데 실패했습니다.', error);
    //         }
    //     };
    //
    //     loadComments();
    // }, []);

    // 댓글 작성
    const saveComment = async () => {
        try {
            if (!authToken) {
                alert("로그인 후에 댓글을 작성할 수 있습니다.");
                return;
            }

            // 현재 시각 구하기
            const commentDate = new Date().toISOString();

            // 레시피 정보 로드
            const response = await axios.get(`http://localhost:8080/api/recipe/${id}`, {
                headers: {
                    'Authorization': `Bearer ${authToken}`
                }
            });

            const recipeData = response.data;
            const recipeId = recipeData.oneRecipeDto.id;
            const memberId = recipeData.oneRecipeDto.authorId;

            // 댓글 저장 API 호출
            const commentResponse = await axios.post("/comments", {
                content: commentInput,
                commentDate: commentDate,
                recipeId: {id},
                memberId: memberId
            });

            const savedComment = commentResponse.data;

            // 저장된 댓글을 화면에 추가
            setComments([...comments, savedComment]);

            // 입력창 초기화
            setCommentInput('');
        } catch (error) {
            console.error("댓글을 작성하는 중 에러 발생:", error);
        }
    };

    // 댓글 수정
    const updateComment = async (commentId) => {
        try {
            if (!authToken) {
                alert("로그인 후에 댓글을 수정할 수 있습니다.");
                return;
            }
            // 댓글 수정 API 호출
            const updatedContent = prompt("댓글을 수정해주세요.", selectedComment.content);
            if (updatedContent) {
                await axios.put(`/comments/${commentId}`, {
                    content: updatedContent
                });
                // 댓글 목록 다시 불러오기
                const response = await axios.get(`/comments`);
                setComments(response.data);
            }
        } catch (error) {
            console.error("댓글을 수정하는 중 에러 발생:", error);
        }
    };

    // 댓글 삭제
    const deleteComment = async (commentId) => {
        try {
            if (!authToken) {
                alert("로그인 후에 댓글을 삭제할 수 있습니다.");
                return;
            }
            // 댓글 삭제 API 호출
            await axios.delete(`/comments/${commentId}`);
            // 삭제된 댓글을 화면에서 제거
            setComments(comments.filter(comment => comment.id !== commentId));
        } catch (error) {
            console.error("댓글을 삭제하는 중 에러 발생:", error);
        }
    };

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
                <div className={'container mx-auto mt-10'}>
                    <div className={'border p-5 rounded-lg'}>
                        {/* 이전 코드는 여기에 */}
                        {/* 댓글 작성 폼 */}
                        <div className="setCommentForm">
                            {/* 로그인 상태에 따라 다른 메시지 표시 */}
                            <input type="textarea" placeholder={authToken ? "댓글을 작성해주세요" : "로그인 후에 댓글을 쓸 수 있습니다."}
                                   value={commentInput} onChange={(e) => setCommentInput(e.target.value)}/>
                            {/* 댓글 작성 버튼 */}
                            <div className="flex justify-end mt-4">
                                <button
                                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mr-2"
                                    onClick={saveComment}
                                >
                                    댓글 작성
                                </button>
                            </div>
                        </div>
                        {/* 댓글 목록 */}
                        <div>
                            {comments.map(comment => (
                                <div key={comment.id}>
                                    <p>{comment.content}</p>
                                    {/* 로그인한 사용자가 작성한 댓글에만 수정 및 삭제 버튼 표시 */}
                                    {/*{authToken && comment.authorId === authToken && (*/}
                                    {authToken && isAuthor === authToken && (
                                        <>
                                            <button className="bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded mr-2"
                                                    onClick={() => {
                                                        setSelectedComment(comment);
                                                        updateComment(comment.id);
                                                    }}>수정</button>
                                            <button className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                                                    onClick={() => deleteComment(comment.id)}>삭제</button>
                                        </>
                                    )}
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RecipeView;