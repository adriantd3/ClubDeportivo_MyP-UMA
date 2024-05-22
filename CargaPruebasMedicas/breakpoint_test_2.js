// Realizado por Adrián Torremocha Doblas y Ezequiel Sánchez García
import http from "k6/http";
import { check } from "k6";

/*
     ✗ status is 200
      ↳  99% — ✓ 305066 / ✗ 1107

     checks.........................: 99.63% ✓ 305065      ✗ 1107
     data_received..................: 88 MB  1.2 MB/s
     data_sent......................: 27 MB  357 kB/s
     http_req_blocked...............: avg=18.39ms  min=0s      med=0s       max=3.52s  p(90)=0s    p(95)=0s
     http_req_connecting............: avg=17.25ms  min=0s      med=0s       max=3.15s  p(90)=0s    p(95)=0s
     http_req_duration..............: avg=686.01ms min=0s      med=538.19ms max=14.14s p(90)=1.45s p(95)=1.67s
       { expected_response:true }...: avg=693.87ms min=468.9µs med=555.62ms max=14.14s p(90)=1.45s p(95)=1.67s
   ✗ http_req_failed................: 1.13%  ✓ 3518        ✗ 307056
     http_req_receiving.............: avg=7.13ms   min=0s      med=0s       max=3.04s  p(90)=497µs p(95)=684.33µs
     http_req_sending...............: avg=1.87ms   min=0s      med=0s       max=2.38s  p(90)=0s    p(95)=500.2µs
     http_req_tls_handshaking.......: avg=0s       min=0s      med=0s       max=0s     p(90)=0s    p(95)=0s
     http_req_waiting...............: avg=677ms    min=0s      med=537.19ms max=14.13s p(90)=1.44s p(95)=1.64s
     http_reqs......................: 310574 4081.855134/s
     iteration_duration.............: avg=997.93ms min=5.32ms  med=813.9ms  max=15.46s p(90)=2.33s p(95)=2.71s
     iterations.....................: 303518 3989.118556/s
     vus............................: 10603  min=0         max=10603
     vus_max........................: 100000 min=18539     max=100000


running (01m16.1s), 000000/100000 VUs, 301378 complete and 10924 interrupted iterations
default ✗ [===>----------------------------------] 004306/100000 VUs  01m09.0s/10m00.0s
ERRO[0085] thresholds on metrics 'http_req_failed' were crossed; at least one has abortOnFail enabled, stopping test prematurely
*/

//EL NUMERO MÑAXIMO DE VUS HA SIDO 10603

export const options = {
  stages: [
    { duration: "10m", target: 100000 }, // just slowly ramp-up to a HUGE load
  ],
  thresholds: {
    http_req_failed: [
      {
        threshold: "rate<=0.01",
        abortOnFail: true,
      },
    ],
  },
};

export default function () {
    let response = http.get("http://localhost:8080/medico/1");

    check(response, {
        "status is 200": (r) => r.status === 200,
    });
}