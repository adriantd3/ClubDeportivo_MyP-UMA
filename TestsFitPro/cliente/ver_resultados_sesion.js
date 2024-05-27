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
    page.goto("http://localhost:8080");
    await page.waitForNavigation();
    sleep(2);

    //Login
    const submitButton = page.locator('button[name="login"]');
    page.locator('input[name="mail"]').clear();
    page.locator('input[name="password"]').clear();
    page.locator('input[name="mail"]').type("mikel@gmail.com");
    page.locator('input[name="password"]').type("kirito");

    await Promise.all([submitButton.click(), page.waitForNavigation()]);

    //Acceder a la primera rutina que tenga asignada
    const rutinaButton = page.locator('button[name="rutinas"]');
    rutinaButton.click();
    await page.waitForNavigation();

    //Acceder a la primera rutina que tenga asignada
    const rutinaLink = page.$$("a")[0];
    rutinaLink.click();
    await page.waitForNavigation();

    //Acceder a la primera sesion de la rutina
    const sesionLink = page.$$("a")[0];
    sesionLink.click();
    await page.waitForNavigation();

    //Acceder al primer desempeño que tenga de estado "Terminado"
    const desempenoLinks = page.$$("a");
    const desempenoLink = desempenoLinks.find( async (link) => {
      return link.evaluate((node) => node.textContent).includes("Terminado");
    });
    desempenoLink.click();

    await page.waitForNavigation();

    check(page, {
      "Numero de tables": p => p.$$("table").length == 2,
    });

  } finally {
    page.close();
  }
}
