
import { Spin } from 'antd';
import React, { useEffect, useState } from 'react'
import DataTable from './DataTable.component';


function ParcelBurList({ printplate_type }) {
    const [overview, setOverview] = useState(null);


    useEffect(() => {
        async function fetchData() {
            let url = '';
            switch (printplate_type) {
                case 0:
                case 1:
                case 2:
                    url = "http://localhost:9092/overview/bur/561/"+printplate_type;
                    break;
                case 3:
                    url = "http://localhost:9092/overview/ns3_bur/561/";
                    break;
                case 4:
                    url = "http://localhost:9092/overview/ns3a_bur/561/";
                    break;
                case 9:
                    url = "http://localhost:9092/overview/condo_encumb/561/";
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
                    problem_desc: 'ไม่มีรหัสภาระผูกพัน',
                    value: response[0].no_BUR_CODE
                },
                {
                    problem_desc: 'ลำดับภาระผูกพันไม่ถูกต้อง (ลำดับมีมากกว่า 3 หลัก)',
                    value: response[0].invalid_LINENO
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

export default ParcelBurList
