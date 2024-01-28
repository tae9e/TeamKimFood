import React, {useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";

function ScoreRecipe(){
    const [inputData, setInputData] = useState([]);

    const fetchRecipes = async () => {
        try {
            const res = await axios.get(`/api/rank/users`);
            setInputData(res.data.content);
        } catch (e) {
            console.error(e.message);
        }
    };

    return(
        <div>
            <h3>유저 랭킹</h3>
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
                                    {rowData.nickname}
                                </td>
                                <td>
                                    {rowData.totalScore}
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td className='listTableIndex'></td>
                            <td className='listTableTitle noData'>충분한 데이터가 없습니다.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
            {/*{Array.from({length: totalPages}, (_, index) => (*/}
            {/*    <button key={index} onClick={() => handlePageChange(index)}>*/}
            {/*        {index + 1}*/}
            {/*    </button>*/}
            {/*))}*/}
        </div>
    );
}
export default ScoreRecipe;