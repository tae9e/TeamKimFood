import React, {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";

function ViewTotalCountRecipe(){
    const [inputData, setInputData] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        fetchRecipes(page);
    }, [page]);

    const fetchRecipes = async (currentPage) => {
        try {
            const res = await axios.get(`http://localhost:8080/api/rank/recipe/totalView?page=${currentPage}&size=10`);
            setInputData(res.data.content);
            setTotalPages(res.data.totalPages);
        } catch (e) {
            console.error(e.message);
        }
    };

    const handlePageChange = (newPage) => {
        setPage(newPage);
    }

    return(
        <div className="container mx-auto px-4 mt-10">
            <h3 className="text-lg font-semibold mb-4">레시피 조회수 랭킹</h3>
            <div className="overflow-x-auto">
                <table className="min-w-full border border-gray-200 divide-y divide-gray-200">
                    <thead>
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">순위</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">제목</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">닉네임</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">조회수</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">작성일</th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {inputData.length > 0 ? (
                        inputData.map((rowData, index) => (
                            <tr key={rowData.id}>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    {index + 1}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <div className="flex items-center">
                                        <div className="flex-shrink-0 h-10 w-10">
                                            <img className="h-10 w-10 rounded-full" src={rowData.imgUrl} alt="레시피사진"/>
                                        </div>
                                        <div className="ml-4">
                                            <Link to={`/api/recipe/${rowData.id}`}
                                                  className="text-sm font-medium text-gray-900">{rowData.title}</Link>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {rowData.nickName}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {rowData.viewCount}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {new Date(rowData.writeDate).toLocaleDateString('ko-KR', {
                                        year: 'numeric',
                                        month: '2-digit',
                                        day: '2-digit'
                                    })}
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="5" className="px-6 py-4 text-center text-sm text-gray-500">작성된 글이 없습니다.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
            <div className="flex justify-center space-x-2 mt-4">
                {Array.from({length: totalPages}, (_, index) => (
                    <button key={index} onClick={() => handlePageChange(index)}
                            className="px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-100">
                        {index + 1}
                    </button>
                ))}
            </div>
        </div>

    );
}

export default ViewTotalCountRecipe;