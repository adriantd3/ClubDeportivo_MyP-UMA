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

    //Login
    const submitButton = page.locator('button[name="login"]');
    page.locator('input[name="mail"]').clear();
    page.locator('input[name="password"]').clear();
    page.locator('input[name="mail"]').type("mikel@gmail.com");
    page.locator('input[name="password"]').type("kirito");
    sleep(2);

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

    //Pulsar boton de nuevo entrenamiento para previsualizar la sesion
    const nuevoEntrenamientoButton = page.locator('button[name="nuevo_entrenamiento"]');
    nuevoEntrenamientoButton.click();
    await page.waitForNavigation();

    //Pulsar boton de comenzar nuevo entrenamiento
    const comenzarEntrenamientoButton = page.locator('button[name="comenzar_entrenamiento"]');
    comenzarEntrenamientoButton.click();
    await page.waitForNavigation();

    //Obtenemos el section del primer ejercicio
    const ejercicio = page.$("section.ejercicio1");

    //Obtenemos el link y su contenido
    const ejercicioButton = ejercicio.$("a");
    const ejercicioName = ejercicioButton.textContent();

    ejercicioButton.click();
    await page.waitForNavigation();

    //Comprobar que hemos accedido a la pagina de ese ejercicio
    check(page, {
      "Se accede a la pagina de ese ejercicio": p => p.locator("header h1").textContent() === ("Información ejercicio: " + ejercicioName.trim()),
    });

  } finally {
    page.close();
  }
}