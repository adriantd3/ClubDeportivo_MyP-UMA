//Realizado por Adrián Torremocha Doblas y Ezequiel Sánchez García
import { browser } from "k6/experimental/browser";
import { check, sleep } from "k6";

export const options = {
  scenarios: {
    ui: {
      executor: "shared-iterations", // para realizar iteraciones sin indicar el tiempo
      options: {
        browser: {
          type: "chromium",
        },
      },
    },
  },
  thresholds: {
    checks: ["rate==1.0"],
  },
};

export default async function () {
  const page = browser.newPage();
  try {
    page.goto("http://localhost:4200/");
    await page.waitForNavigation();

    const submitButton = page.locator('button[name="login"]');
    page.locator('input[name="nombre"]').clear();
    page.locator('input[name="DNI"]').clear();
    page.locator('input[name="nombre"]').type("Pedro");
    page.locator('input[name="DNI"]').type("1");

    await Promise.all([submitButton.click()]);

    await page.waitForSelector("table tbody tr");
    const paciente = page.$$("table tbody tr")[0];
    await Promise.all([
      paciente.click(),
      page.waitForNavigation(), // Esperar a que se complete la navegación
    ]);

    await Promise.all([
      page.locator('button[name="view"]').click(),
      page.waitForNavigation(), // Esperar a que se complete la navegación
    ]);

    page.locator('button[name="predict"]').click();
    await page.waitForSelector('span[name="predict"]');

    const spanText = page.locator('span[name="predict"]').textContent();
    check(spanText, {
      "Texto de resultado aparece": spanText === "Probabilidad de cáncer:"
    });

  } finally {
    page.close();
  }
}
