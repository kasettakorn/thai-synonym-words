
import { Spin } from 'antd';
import React, { useEffect, useState } from 'react'
import DataTable from './DataTable.component';


function ParcelHDList({ printplate_type }) {
    const [overview, setOverview] = useState(null);


    useEffect(() => {
        async function fetchData() {
            let url = '';
            switch (printplate_type) {
                case 0:
                case 1:
                case 2:
                    url = "http://localhost:9092/overview/dt/561/"+printplate_type;
                    break;
                case 3:
                    url = "http://localhost:9092/overview/ns3dt/561/";
                    break;
                case 4:
                    url = "http://localhost:9092/overview/ns3adt/561/";
                    break;
                case 9:
                    url = "http://localhost:9092/overview/owner_condo/561/";
                    break;
                case 13:
                    url = "http://localhost:9092/overview/condo/561/";
                    break;
                default:
                    break;
            }

            const response = await fetch(url)
                .then(response => response.json())
                .then(data => {
                    return data;
                });
            console.log(response);
            setOverview([
                {
                    problem_desc: 'ไม่มีรหัสและชื่อการได้มาครั้งสุดท้าย',
                    value: response[0].no_RECV_CODE
                },
                {
                    problem_desc: 'ไม่มีรหัสและชื่อการจดทะเบียนครั้งสุดท้าย',
                    value: response[0].no_REG_CODE
                },
                {
                    problem_desc: 'ไม่มีชื่อจังหวัดในข้อมูลที่อยู่ของผู้ถือกรรมสิทธิ์',
                    value: response[0].no_OWN_PROV
                },
                {
                    problem_desc: 'ไม่มีชื่ออำเภอในข้อมูลที่อยู่ของผู้ถือกรรมสิทธิ์',
                    value: response[0].no_OWN_AMPHUR
                },
                {
                    problem_desc: 'ไม่มีชื่อตำบลในข้อมูลที่อยู่ของผู้ถือกรรมสิทธิ์',
                    value: response[0].no_OWN_TUMB
                },
                {
                    problem_desc: 'ไม่มีคำนำหน้าชื่อในข้อมูลของผู้ถือกรรมสิทธิ์',
                    value: response[0].no_TITLE
                },
                {
                    problem_desc: 'ไม่มีคำนำหน้าชื่อบิดาของผู้ถือกรรมสิทธิ์',
                    value: response[0].no_FAT_TITLE
                },
                {
                    problem_desc: 'ไม่มีคำนำหน้าชื่อมารดาของผู้ถือกรรมสิทธิ์',
                    value: response[0].no_MOT_TITLE
                },
                {
                    problem_desc: 'ข้อมูลอายุไม่ถูกต้อง (ข้อมูลไม่อยู่ระหว่าง 1-999)',
                    value: response[0].wrong_AGE
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

export default ParcelHDList
