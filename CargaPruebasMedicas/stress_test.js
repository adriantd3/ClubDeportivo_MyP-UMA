import http from 'k6/http';
import { sleep } from 'k6';
import { check } from 'k6';

// Numero máximo de VUs conseguidos en la prueba de rotura sin executors (en mi equipo) : 11359

/*       /\      |‾‾| /‾‾/   /‾‾/
     /\  /  \     |  |/  /   /  /
    /  \/    \    |     (   /   ‾‾\
   /          \   |  |\  \ |  (‾)  |
  / __________ \  |__| \__\ \_____/ .io

     execution: local
        script: .\stress_test.js
 web dashboard: http://127.0.0.1:5665
        output: -

     scenarios: (100.00%) 1 scenario, 8000 max VUs, 8m30s max duration (incl. graceful stop):
              * default: Up to 8000 looping VUs for 8m0s over 3 stages (gracefulRampDown: 30s, gracefulStop: 30s)


     ✓ Response code was 200

     checks.........................: 100.00% ✓ 2689950     ✗ 0
     data_received..................: 770 MB  1.6 MB/s
     data_sent......................: 237 MB  493 kB/s
     http_req_blocked...............: avg=194.21µs min=0s med=0s    max=534.91ms p(90)=0s    p(95)=0s
     http_req_connecting............: avg=174.16µs min=0s med=0s    max=534.91ms p(90)=0s    p(95)=0s
   ✓ http_req_duration..............: avg=979.83ms min=0s med=1.12s max=9.14s    p(90)=1.37s p(95)=1.49s
       { expected_response:true }...: avg=979.83ms min=0s med=1.12s max=9.14s    p(90)=1.37s p(95)=1.49s
   ✓ http_req_failed................: 0.00%   ✓ 0           ✗ 2689950
     http_req_receiving.............: avg=4.5ms    min=0s med=0s    max=704.19ms p(90)=2.6ms p(95)=21.92ms
     http_req_sending...............: avg=148.09µs min=0s med=0s    max=705.75ms p(90)=0s    p(95)=0s
     http_req_tls_handshaking.......: avg=0s       min=0s med=0s    max=0s       p(90)=0s    p(95)=0s
     http_req_waiting...............: avg=975.18ms min=0s med=1.12s max=9.14s    p(90)=1.36s p(95)=1.48s
     http_reqs......................: 2689950 5604.177188/s
     iteration_duration.............: avg=982.22ms min=0s med=1.12s max=9.14s    p(90)=1.38s p(95)=1.5s
     iterations.....................: 2689950 5604.177188/s
     vus............................: 39      min=20        max=8000
     vus_max........................: 8000    min=8000      max=8000

                                                                                                                                       
running (8m00.0s), 0000/8000 VUs, 2689950 complete and 0 interrupted iterations                                                        
default ✓ [======================================] 0000/8000 VUs  8m0s */

export const options = {
    stages: [
    { duration: '3m', target: 8000 }, // subimos a 8000 VUs en 3 minutos
    { duration: '3m', target: 8000 }, // nos mantenemos en 8000 VUs durante 3 minutos
    { duration: '2m', target: 0 }, // bajamos a 0 VUs
    ],
    thresholds: {
        http_req_failed: [{
        threshold: 'rate<=0.01', // El porcentaje de solicitudes fallidas debe ser menor al 1%
        abortOnFail: true, // En cualquier otro caso, se aborta el test
        }],
        http_req_duration: ['avg<=1000'], // El promedio de la duración de las peticiones debe de ser inferior a 1000ms
    },
    };

export default () => {
    let response = http.get('http://localhost:8080/medico/1'); // URL del servicio web a probar
    
    // Comprobamos que el codigo de respuesta es exitoso (código 200)
    check(response, {
        'Response code was 200': (res) => res.status == 200,
        });
    
};
