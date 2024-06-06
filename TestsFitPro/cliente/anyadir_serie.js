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

    //Pulsar boton de nuevo entrenamiento
    const nuevoEntrenamientoButton = page.locator('button[name="nuevo_entrenamiento"]');
    nuevoEntrenamientoButton.click();
    await page.waitForNavigation();

    //Pulsar boton de comenzar nuevo entrenamiento para previsualizar la sesion
    const comenzarEntrenamientoButton = page.locator('button[name="comenzar_entrenamiento"]');
    comenzarEntrenamientoButton.click();
    await page.waitForNavigation();

    //Obtenemos el section del primer ejercicio
    const ejercicio = page.$("section.ejercicio1");

    //Calculamos el número de series y obtenemos el botón de editar
    const numeroDeSeries = ejercicio.$$("table tbody tr").length;
    const anyadirButton = ejercicio.$("button[name='nueva_serie']");

    //Pulsar boton de editar
    anyadirButton.click();
    await page.waitForNavigation();

    //Modificar los valores de la serie
    const guardarButton = page.locator('button[name="guardar"]');
    const input1 = page.locator('input[id="input1"]');
    const input2 = page.locator('input[id="input2"]');

    input1.clear();
    input2.clear();
    input1.type("10");
    input2.type("15");
    sleep(3);

    //Pulsamos el boton de guardar
    await Promise.all([guardarButton.click(), page.waitForNavigation()]);

    //Comprobar que se ha creado un nuevo desempeño
    check(page, {
      "Se incrementa el número de series": p => p.$("section.ejercicio1").$$("table tbody tr").length == numeroDeSeries + 1,
    });

  } finally {
    page.close();
  }
}