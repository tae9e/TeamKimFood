import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {useNavigate} from "react-router-dom";

const RecipeList = () => {
    const [recipes, setRecipes] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const navigate = useNavigate();
    const authToken = localStorage.getItem('token');
    useEffect(() => {
        const fetchRecipes = async () => {
            try{
                const response = await axios.get(`/api/recipes/boardList?page=${page}`,{
                    headers: {
                        'Authorization': `Bearer ${authToken}`
                    }
                }
                );

                setRecipes(response.data.content);
                setTotalPages(response.data.totalPages);
            } catch (error){
                console.error('레시피 에러가 발생했습니다.', error);
            }

        };

        fetchRecipes();
    }, [page]);

    return (
        <div className={'flex flex-col min-h-screen container mx-auto mt-10'}>
            <div className={"flex flex-wrap justify-center gap-4"}>
                {recipes.map((recipe, index) => (
                    <RecipeItem key={recipe.id} recipe={recipe} navigate={navigate}/>
                ))}
                <Pagination currentPage={page} setPage={setPage} totalPages={totalPages} />
            </div>
        </div>
    );
};

const RecipeItem = ({ recipe, navigate, currentPage }) => {
    const imageUrl = `${process.env.PUBLIC_URL}${recipe.imgUrl}`;
    const handleTitleClick = () => {
        navigate(`/api/recipe/${recipe.id}`, { state: { fromPage: currentPage } })
    }
    if (!recipe){
        return <div>표시할 레시피가 없습니다.</div>;
    } else {
        return (
            <div className={"border rounded-lg p-4 w-60 text-center "}>
                <img onClick={handleTitleClick} src={imageUrl} alt={recipe.title} className={" cursor-pointer w-full h-40 object-cover rounded-t-lg"}/>
                <div className={"border-t my-2 "}></div>
                <h3 onClick={handleTitleClick}
                    className={"cursor-pointer mt-2 text-lg font-semibold font-[SpoqaHanSansNeo-B]"}>{recipe.title}</h3>
                <div>

                    <span className={"text-sm font-[NanumMyeongjo]"}>{formatDate(recipe.writeDate)}</span>
                </div>
                <div className="flex justify-between items-center mt-2 text-sm font-[GmarketSans-M]">
                    <span>조회수: {recipe.viewCount}</span>
                    <span>{recipe.nickName}</span>
                </div>
            </div>
        );
    }
};
const formatDate = (dateString) => {
    const options = {year: '2-digit', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
    return new Date(dateString).toLocaleDateString('ko-KR', options);
};

const Pagination = ({ currentPage, setPage, totalPages }) => {
    // 이전 페이지 이동 함수
    const handlePrevious = () => {
        if (currentPage > 0) {
            setPage(currentPage - 1);
        } else {
            alert('이전 페이지가 없습니다.');
        }
    };

    // 다음 페이지 이동 함수
    const handleNext = () => {
        if (currentPage < totalPages - 1) {
            setPage(currentPage + 1);
        } else {
            alert('다음 페이지가 없습니다.');
        }
    };

    // 페이지 번호 버튼을 렌더링하는 함수
    const renderPageNumbers = () => {
        const pageNumbers = [];
        for (let i = 0; i < totalPages; i++) {
            pageNumbers.push(
                <button key={i} onClick={() => setPage(i)} disabled={i === currentPage}
                        className={`mx-1 px-3 py-2 text-sm font-medium rounded focus:outline-none focus:ring-2 focus:ring-blue-300 ${
                            i === currentPage ? 'bg-blue-500 text-white' : 'bg-gray-300 hover:bg-gray-400 text-black'
                        }`}>
                    {i + 1}
                </button>
            );
        }
        return pageNumbers;
    };

    return (
        <div className={"flex justify-center items-center mt-auto p-4 border-t border-gray-200 bg-gray-100 w-full"}>
            <button onClick={handlePrevious}
            className={"bg-gray-300 hover:bg-gray-400 text-black font-bold py-2 px-6 rounded mx-2"}
            >이전</button>
            {renderPageNumbers()}
            <button onClick={handleNext}
            className={"bg-gray-300 hover:bg-gray-400 text-black font-bold py-2 px-6 rounded mx-2"}
            >다음</button>
        </div>
    );
};

export default RecipeList;