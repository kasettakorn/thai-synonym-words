import React, { useEffect } from 'react';
import '../styles/datatable.css';

function DataTable(props) {
    useEffect(() => {

        console.log(props.data);
    }, [])

    return (
        <div className='centered'>
            <table className="table table-bordered table-hover overview-table">
                <thead>
                    <tr>
                        <th>ลำดับ</th>
                        <th>รายการข้อผิดพลาด</th>
                        <th>จำนวนเอกสารสิทธิ</th>
                    </tr>
                </thead>
                <tbody>
                    {props.data.map((data, i) => {
                        return <tr key={i}>
                            <th>{i + 1}</th>
                            <td>{data.problem_desc}</td>
                            <td>{Number(data.value).toLocaleString()}</td>
                        </tr>
                    })}
                </tbody>
            </table>
        </div>

    )
}

export default DataTable