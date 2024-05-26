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

    page.locator('button[name="add"]').click();
    await page.waitForNavigation();

    page.locator("textarea").clear();
    page.locator("textarea").type("Informe de la imagen");
    
    await Promise.all([
      page.locator('button[name="save"]').click(),
      page.waitForNavigation(), // Esperar a que se complete la navegación
    ]);

    await page.waitForSelector('div.info-item');

    check(page, {
      "Creación de informe": page =>
        page.$$("div.info-item").length === 3,
      "Texto de informe": page => 
        page.locator('span[name="content"]').textContent() === "Informe de la imagen", 
    });

  } finally {
    page.close();
  }
}
