import { Badge } from 'antd';
import React from 'react';
import '../styles/courses.css';
export default function Card({ title, description, url }) {
    return (
        <div className="card" style={{ height: '100%' }}>
            {!title.includes("Scratch") && !title.includes("Web") ?
                <iframe
                    className="card-img-top"
                    width="560"
                    height="255"
                    src={url}
                    title="YouTube video player"
                    frameBorder="0"
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                    allowFullScreen></iframe> :
                <Badge.Ribbon text="Coming soon" color="red">
                    <img src={url} alt="video" width="560"
                        height="255" className="card-img-top" />
                </Badge.Ribbon>


            }

            <div className="card-body">
                <h4 className="card-title">{title}</h4>
            </div>

        </div>
    )
}
