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
            const res = await axios.get(`/api/recipe/recipeTotalView?page=${currentPage}&size=10`);
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
        <div>
            <h3>조회수 랭킹</h3>
            <div>
                <table className='listTable'>
                    <tbody>
                    <tr>
                        <td className='listTableIndex th'>index</td>
                        <td className='listTableTitle th'>title</td>
                    </tr>
                    {inputData.length > 0 ? (
                        inputData.map(rowData => (
                            <tr key={rowData.recipe_id}>
                                <td>
                                    <img src={rowData.imgUrl} alt="레시피사진"/>
                                </td>
                                <td className='listTableIndex'>
                                    <Link to={`/BoardContent/${rowData.recipeId}`}>{rowData.recipeId}</Link>
                                </td>
                                <td className='listTableTitle'>
                                    <Link to={`/BoardContent/${rowData.recipeId}`}>{rowData.title}</Link>
                                </td>
                                <td>
                                    {rowData.nickname}
                                </td>
                                <td>
                                    {rowData.viewCount}
                                </td>
                                <td>
                                    {rowData.writeDate}
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                        <td className='listTableIndex'></td>
                            <td className='listTableTitle noData'>작성된 글이 없습니다.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
            {Array.from({length: totalPages}, (_, index) => (
                <button key={index} onClick={() => handlePageChange(index)}>
                    {index + 1}
                </button>
            ))}
        </div>
    );
}
export default ViewTotalCountRecipe;