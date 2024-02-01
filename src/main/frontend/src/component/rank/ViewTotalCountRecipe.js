import React, {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";

function ViewTotalCountRecipe(){
    const [inputData, setInputData] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    // const [inputData, setInputData] = useState([{
    //     view_count: '',
    //     recipe_id: '',
    //     imgUrl:'',
    //     title: '',
    //     nick_name: '',
    //     write_date:''
    // }]);
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

    // const [lastIdx, setLastIdx] = useState(0)
    //
    // useEffect(async () => {
    //     try {
    //         // 데이터를 받아오는 동안 시간이 소요되므로 await 로 대기
    //         const res = await axios.get("/api/recipe/recipeTotalView")
    //
    //         // 받아온 데이터로 다음 작업을 진행하기 위해 await 로 대기
    //         // 받아온 데이터를 map 해주어 rowData 별로 _inputData 선언
    //         const _inputData = await res.data.map((rowData) => ({
    //             view_count: rowData.viewCount,
    //             recipe_id: rowData.id,
    //             imgUrl:rowData.imgUrl,
    //             title: rowData.title,
    //             nick_name: rowData.nickname,
    //             write_date: rowData.writeDate
    //             })
    //         )
    //         // 선언된 _inputData 를 최초 선언한 inputData 에 concat 으로 추가
    //         setInputData(inputData.concat(_inputData))
    //     } catch (e) {
    //         console.error(e.message)
    //     }
    // }, [])

    return(
        <div>
            <h3>레시피 조회수 랭킹</h3>
            <div>
                <table className='listTable'>
                    <tbody>
                    <tr>
                        <td className='listTableIndex th'>index</td>
                        <td className='listTableTitle th'>title</td>
                    </tr>
                    {inputData.length > 0 ? (
                        inputData.map(rowData => (
                            <tr key={rowData.id}>
                                <td>
                                    <img src={rowData.imgUrl} alt="레시피사진"/>
                                </td>
                                <td className='listTableIndex'>
                                    <Link to={`/BoardContent/${rowData.id}`}>{rowData.id}</Link>
                                </td>
                                <td className='listTableTitle'>
                                    <Link to={`/BoardContent/${rowData.id}`}>{rowData.title}</Link>
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