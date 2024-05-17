import { check } from "k6";
import http from "k6/http";

/*
✗ status is 200
      ↳  99% — ✓ 3734113 / ✗ 1490

     checks.........................: 99.96%  ✓ 3734113     ✗ 1490
     data_received..................: 1.1 GB  1.8 MB/s
     data_sent......................: 329 MB  548 kB/s
     dropped_iterations.............: 2228232 3713.133976/s
     http_req_blocked...............: avg=523.84µs min=0s med=0s       max=1.42s  p(90)=0s       p(95)=0s
     http_req_connecting............: avg=453.37µs min=0s med=0s       max=1.31s  p(90)=0s       p(95)=0s
     http_req_duration..............: avg=185.76ms min=0s med=96.05ms  max=3.42s  p(90)=501.49ms p(95)=675.28ms
       { expected_response:true }...: avg=185.83ms min=0s med=96.13ms  max=3.42s  p(90)=501.61ms p(95)=675.34ms
   ✓ http_req_failed................: 0.03%   ✓ 1490        ✗ 3734113
     http_req_receiving.............: avg=5.33ms   min=0s med=0s       max=2.08s  p(90)=500.7µs  p(95)=1.03ms
     http_req_sending...............: avg=2.45ms   min=0s med=0s       max=1.4s   p(90)=0s       p(95)=216.08µs
     http_req_tls_handshaking.......: avg=0s       min=0s med=0s       max=0s     p(90)=0s       p(95)=0s
     http_req_waiting...............: avg=177.97ms min=0s med=91.52ms  max=3.42s  p(90)=481.85ms p(95)=655.28ms
     http_reqs......................: 3735603 6225.022538/s
     iteration_duration.............: avg=234.37ms min=0s med=136.52ms max=14.14s p(90)=605.48ms p(95)=768.33ms
     iterations.....................: 3735603 6225.022538/s
     vus............................: 52      min=0         max=6630
     vus_max........................: 6678    min=1000      max=6678


running (10m00.1s), 00000000/00006678 VUs, 3735603 complete and 0 interrupted iterations
breakpoint ✓ [======================================] 00000000/00006678 VUs  10m0s  044587.12 iters/s
*/

export const options = {
  scenarios: {
    breakpoint: {
      executor: "ramping-arrival-rate", // Incrementa la carga exponencial
      preAllocatedVUs: 1000, //VUs alocados inicialmente
      maxVUs: 1e7, //VUs maximo
      stages: [
        { duration: "10m", target: 100000 }, // just slowly ramp-up to a
      ],
    },
  },
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
