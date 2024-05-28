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

    sleep(1);

    //Login
    const submitButton = page.locator('button[name="login"]');
    page.locator('input[name="mail"]').clear();
    page.locator('input[name="password"]').clear();
    page.locator('input[name="mail"]').type("maria@gmail.com");
    page.locator('input[name="password"]').type("maria");
    sleep(1);

    await Promise.all([submitButton.click(), page.waitForNavigation()]);

    // Compruebo que estoy en la pagina home del entrenador
    check(page, {
        'Redireccion a página home': p => p.locator('h1').textContent() == 'Bienvenida, María',
      });

    sleep(1);

    //Localizamos el boton de rutinas
    const rutinasButton = page.locator('button[name="rutinas"]');

    // Pulsamos el boton de clientes
    await Promise.all([rutinasButton.click(), page.waitForNavigation()]);

    sleep(1);

    // Comprobamos que estamos en la pagina de rutinas del entrenador
    check(page, {
        'Redireccion a página rutinas': p => p.locator('h1').textContent() == 'Rutinas',
      });

    
    // Añadimos una rutina
    const numeroRutinas = page.$$("table tbody tr").length;

    // Pulsamos el boton de añadir rutina para que nos salte un pop up en el que insertaremos el nombre 
    // de la nueva rutina
    const anyadirRutinaButton = page.locator('button[name="anyadir_rutina"]');
    await Promise.all([anyadirRutinaButton.click(), page.waitForNavigation()]);

    sleep(1);

    /* NO FUNCIONA DE MOMENTO

    // Insertamos el nombre de la rutina
    const nombreRutinaInput = page.locator('#nuevaRutina > div > div > div.modal-body > form > input[type="text"]');
    await nombreRutinaInput.fill('RUTINA DE PRUEBA');


    const guardarRutinaButton = page.locator('button[name="guardar_rutina"]');
    await Promise.all([guardarRutinaButton.click(), page.waitForNavigation()]);

    sleep(1);
    
    // Comprobamos que le numero de rutinas haya aumentado
    check(page, {
        'Se aumenta el numero de rutinas': p => p.$$("table tbody tr").length == numeroRutinas+1,
      });

      */


  } finally {
    page.close();
  }
}