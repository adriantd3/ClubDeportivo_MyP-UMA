// Realizado por Adrián Torremocha Doblas y Ezequiel Sánchez García
import http from 'k6/http';
import { sleep } from 'k6';
import { check } from 'k6';

/*    
          /\      |‾‾| /‾‾/   /‾‾/
     /\  /  \     |  |/  /   /  /
    /  \/    \    |     (   /   ‾‾\
   /          \   |  |\  \ |  (‾)  |
  / __________ \  |__| \__\ \_____/ .io

     execution: local
        script: .\smoke_test.js
 web dashboard: http://127.0.0.1:5665
        output: -

     scenarios: (100.00%) 1 scenario, 5 max VUs, 1m30s max duration (incl. graceful stop):
              * default: 5 looping VUs for 1m0s (gracefulStop: 30s)


     ✓ Response code was 200

     checks.........................: 100.00% ✓ 167010      ✗ 0
     data_received..................: 48 MB   797 kB/s
     data_sent......................: 15 MB   245 kB/s
     http_req_blocked...............: avg=9.36µs   min=0s med=0s     max=20.57ms p(90)=0s      p(95)=0s
     http_req_connecting............: avg=5.97µs   min=0s med=0s     max=8.92ms  p(90)=0s      p(95)=0s
   ✓ http_req_duration..............: avg=1.69ms   min=0s med=1.65ms max=28.49ms p(90)=2.29ms  p(95)=2.72ms
       { expected_response:true }...: avg=1.69ms   min=0s med=1.65ms max=28.49ms p(90)=2.29ms  p(95)=2.72ms
   ✓ http_req_failed................: 0.00%   ✓ 0           ✗ 167010
     http_req_receiving.............: avg=220.57µs min=0s med=0s     max=26.09ms p(90)=579.2µs p(95)=743.75µs
     http_req_sending...............: avg=12.1µs   min=0s med=0s     max=8.01ms  p(90)=0s      p(95)=0s
     http_req_tls_handshaking.......: avg=0s       min=0s med=0s     max=0s      p(90)=0s      p(95)=0s
     http_req_waiting...............: avg=1.46ms   min=0s med=1.42ms max=20.75ms p(90)=1.94ms  p(95)=2.3ms
     http_reqs......................: 167010  2783.809351/s
     iteration_duration.............: avg=1.78ms   min=0s med=1.71ms max=29.14ms p(90)=2.4ms   p(95)=2.86ms
     iterations.....................: 167010  2783.809351/s
     vus............................: 5       min=5         max=5
     vus_max........................: 5       min=5         max=5

                                                                                                                                       
running (1m00.0s), 0/5 VUs, 167010 complete and 0 interrupted iterations                                                               
default ✓ [======================================] 5 VUs  1m0s */

export const options = {
    vus: 5, // Número de usuarios virtuales: 5
    duration: '1m', // Duración: 1 min
    thresholds: {
        http_req_failed: [{
        threshold: 'rate==0', // No debe haber peticiones fallidas
        abortOnFail: true, // En cualquier otro caso se aborta el test
        }],
        http_req_duration: ['avg<=100'], // El promedio de la duración de las peticiones debe ser inferior a 100ms
    },
};

export default () => {
    let response = http.get('http://localhost:8080/medico/1'); // URL del servicio web a probar
    
    // Comprobamos que el codigo de respuesta es exitoso (código 200)
    check(response, {
        'Response code was 200': (res) => res.status == 200,
        });

};
