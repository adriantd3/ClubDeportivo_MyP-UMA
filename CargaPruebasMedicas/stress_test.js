import http from 'k6/http';
import { sleep } from 'k6';
import { check } from 'k6';

// Numero máximo de VUs conseguidos en la prueba de rotura (en mi equipo) : 11359

/*       /\      |‾‾| /‾‾/   /‾‾/
     /\  /  \     |  |/  /   /  /
    /  \/    \    |     (   /   ‾‾\
   /          \   |  |\  \ |  (‾)  |
  / __________ \  |__| \__\ \_____/ .io

     execution: local
        script: .\stress_test.js
 web dashboard: http://127.0.0.1:5665
        output: -

     scenarios: (100.00%) 1 scenario, 8200 max VUs, 8m30s max duration (incl. graceful stop):
              * default: Up to 8200 looping VUs for 8m0s over 3 stages (gracefulRampDown: 30s, gracefulStop: 30s)


     ✓ Response code was 200

     checks.........................: 100.00% ✓ 2160488     ✗ 0
     data_received..................: 618 MB  1.3 MB/s
     data_sent......................: 190 MB  396 kB/s
     http_req_blocked...............: avg=237.1µs  min=0s med=0s    max=515.25ms p(90)=0s     p(95)=0s
     http_req_connecting............: avg=214.87µs min=0s med=0s    max=515.25ms p(90)=0s     p(95)=0s
   ✗ http_req_duration..............: avg=1.25s    min=0s med=1.4s  max=9.06s    p(90)=1.81s  p(95)=1.95s
       { expected_response:true }...: avg=1.25s    min=0s med=1.4s  max=9.06s    p(90)=1.81s  p(95)=1.95s
   ✓ http_req_failed................: 0.00%   ✓ 0           ✗ 2160488
     http_req_receiving.............: avg=5.86ms   min=0s med=0s    max=933.45ms p(90)=5.01ms p(95)=28.92ms
     http_req_sending...............: avg=202.89µs min=0s med=0s    max=630.32ms p(90)=0s     p(95)=0s
     http_req_tls_handshaking.......: avg=0s       min=0s med=0s    max=0s       p(90)=0s     p(95)=0s
     http_req_waiting...............: avg=1.24s    min=0s med=1.4s  max=9.06s    p(90)=1.8s   p(95)=1.93s
     http_reqs......................: 2160488 4501.123958/s
     iteration_duration.............: avg=1.25s    min=0s med=1.41s max=9.06s    p(90)=1.81s  p(95)=1.96s
     iterations.....................: 2160488 4501.123958/s
     vus............................: 46      min=16        max=8200
     vus_max........................: 8200    min=8200      max=8200

                                                                                                                                       
running (8m00.0s), 0000/8200 VUs, 2160488 complete and 0 interrupted iterations                                                        
default ✓ [======================================] 0000/8200 VUs  8m0s                                                                 
ERRO[0488] thresholds on metrics 'http_req_duration' have been crossed */

// Ha fallado el promedio de la duracion de las peticiones

export const options = {
    stages: [
    { duration: '3m', target: 8200 }, // subimos a 80000 VUs en 3 minutos
    { duration: '3m', target: 8200 }, // nos mantenemos en 80000 VUs durante 3 minutos
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
