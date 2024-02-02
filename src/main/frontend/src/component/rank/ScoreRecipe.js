import React, {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";

function ScoreRecipe(){
    const [inputData, setInputData] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        fetchRecipes(page);
    }, [page]);

    const fetchRecipes = async (currentPage) => {
        try {
            const res = await axios.get(`http://localhost:8080/api/rank/recipe/recommend?page=${currentPage}&size=10`);
            setInputData(res.data.content);
            setTotalPages(res.data.totalPages);
        } catch (e) {
            console.error(e.message);
        }
    };

    const handlePageChange = (newPage) => {
        setPage(newPage);
    }

    return (
        <div className="container mx-auto px-4 sm:px-8">
            <h3 className="text-xl font-semibold leading-tight my-6">레시피 추천수 랭킹</h3>
            <div className="py-4">
                <div className="shadow overflow-hidden rounded border-b border-gray-200">
                    <table className="min-w-full bg-white">
                        <thead className="bg-blue-300 text-white">
                        <tr>
                            <th className="text-left py-3 px-4 uppercase font-semibold text-sm">순위</th>
                            <th className="text-left py-3 px-4 uppercase font-semibold text-sm">제목</th>
                            <th className="text-left py-3 px-4 uppercase font-semibold text-sm">작성자</th>
                            <th className="text-left py-3 px-4 uppercase font-semibold text-sm">조회수</th>
                            <th className="text-left py-3 px-4 uppercase font-semibold text-sm">글쓴일자</th>
                        </tr>
                        </thead>
                        <tbody className="text-gray-700">
                        {inputData.length > 0 ? (
                            inputData.map((rowData, index) => (
                                <tr key={rowData.recipe_id}>
                                    <td className="text-left py-3 px-4">{index + 1}</td>
                                    <td className="text-left py-3 px-4">
                                        <div className="flex items-center">
                                            <div className="flex-shrink-0 h-20 w-20"> {/* 크기 조정 */}
                                                <img className="h-20 w-20 rounded-full" src={rowData.imgUrl}
                                                     alt="레시피사진"/>
                                            </div>
                                            <div className="ml-4">
                                                <Link to={`/BoardContent/${rowData.recipeId}`}
                                                      className="text-sm font-medium text-gray-900">{rowData.title}</Link>
                                            </div>
                                        </div>
                                    </td>
                                    <td className="text-left py-3 px-4">{rowData.nickName}</td>
                                    <td className="text-left py-3 px-4">{rowData.viewCount}</td>
                                    <td className="text-left py-3 px-4">{new Date(rowData.writeDate).toLocaleDateString()}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="5" className="text-center py-3 px-4">작성된 글이 없습니다.</td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
                <div className="flex flex-wrap -mx-1 overflow-hidden sm:-mx-1 md:-mx-1 lg:-mx-1 xl:-mx-1">
                    {Array.from({length: totalPages}, (_, index) => (
                        <div key={index}
                             className="my-1 px-1 w-1/4 overflow-hidden sm:my-1 sm:px-1 sm:w-1/4 md:my-1 md:px-1 md:w-1/4 lg:my-1 lg:px-1 lg:w-1/4 xl:my-1 xl:px-1 xl:w-1/4">
                            <button onClick={() => handlePageChange(index)}
                                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                                {index + 1}
                            </button>
                        </div>
                    ))}
                </div>
            </div>
        </div>

    );
}

export default ScoreRecipe;