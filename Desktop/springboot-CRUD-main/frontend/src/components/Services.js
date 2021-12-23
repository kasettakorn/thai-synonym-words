import React  from 'react';
import Title from './Title';

export default function Services({ services, title }) {
    return (
        <div className="services">
            <Title title={title} />
            <div className="services-center">
                {services.map((service, i) => {
                    return <div key={i} className="service">
                        <span>{service.icon}</span>
                        <h6>{service.title}</h6>
                        <p>{service.info}</p>
                    </div>
                })}
            </div>        
        </div>
    )
}