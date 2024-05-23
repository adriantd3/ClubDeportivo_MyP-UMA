// Realizado por Adrián Torremocha Doblas y Ezequiel Sánchez García
import http from "k6/http";
import { check } from "k6";

const maxVu = 10603;

export const options = {
  stages: [
    { duration: "3m", target: Math.ceil(maxVu*0.5) },
    { duration: "3m", target: Math.ceil(maxVu*0.5) },
    { duration: "2m", target: 0 },
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
