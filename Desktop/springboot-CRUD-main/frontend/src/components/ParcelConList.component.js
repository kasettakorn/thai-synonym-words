
import { Spin } from 'antd';
import React, { useEffect, useState } from 'react'
import DataTable from './DataTable.component';


function ParcelCONList({ printplate_type }) {
    const [overview, setOverview] = useState(null);


    useEffect(() => {
        async function fetchData() {
            let url = '';
            switch (printplate_type) {
                case 0:
                case 1:
                case 2:
                    url = "http://localhost:9092/overview/con/561/"+printplate_type;
                    break;
                case 3:
                    url = "http://localhost:9092/overview/ns3_con/561/";
                    break;
                case 4:
                    url = "http://localhost:9092/overview/ns3a_con/561/";
                    break;
                default:
                    break;
            }

            const response = await fetch(url)
                .then(response => response.json())
                .then(data => {
                    return data;
                });
            setOverview([
                {
                    problem_desc: 'ไม่มีรหัสสิ่งปลูกสร้างในข้อมูลสิ่งปลูกสร้าง',
                    value: response[0].no_CONSTR_CODE
                },
                {
                    problem_desc: 'รหัสสิ่งปลูกสร้างไม่ถูกต้องตามประเภทสิ่งปลูกสร้าง (ลำดับมีมากกว่า 3 หลัก)',
                    value: response[0].no_CON_CONSTR_CODE
                }
            ]);

        }
        fetchData();
    }, [])


    if (!overview) return <div style={{ position: "absolute", top: "35%", left: "50%" }}>

        <Spin size='large' />

    </div>

    return (
        <div>
            <DataTable data={overview} />
        </div>
    )
}

export default ParcelCONList
