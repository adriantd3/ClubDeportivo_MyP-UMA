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
    const sesionesButton = page.locator('button[name="sesiones"]');

    // Pulsamos el boton de clientes
    await Promise.all([sesionesButton.click(), page.waitForNavigation()]);

    // Comprobamos que estamos en la pagina de sesiones del entrenador
    check(page, {
        'Redireccion a página sesiones': p => p.locator('h1').textContent() == 'Sesiones',
      });
    
    // Accedemos a la ultima sesion de la tabla
    const nombreSesion = page.$('table tbody tr:last-child td[name="nombre_sesion"]').textContent().trim();

    const ultimaSesionButton = page.$('table tbody tr:last-child button[name="editar"]');
    await Promise.all([ultimaSesionButton.click(), page.waitForNavigation()]);

    // Recogemos el numero de ejercicios actuales para despues comprobar que se ha sumado un ejercicio
    const numeroEjercicios = page.$$('table[name="tabla_ejercicio"]').length;

    sleep(2);

    // Comprobamos que estamos en la pagina de editar la sesion
    check(page, {
        'Redireccion a editar ultima sesion': p => p.locator('h1').textContent().trim().includes(nombreSesion),
      });

    const anyadirEjercicioButton = page.locator('button[name="anyadir_ejercicio"]');
    await Promise.all([anyadirEjercicioButton.click(), page.waitForNavigation()]);

    sleep(1);

    // Comprobamos que estamos en la pagina de seleccionar ejercicio
    check(page, {
        'Redireccion a seleccionar ejercicio': p => p.locator('h5').textContent() == 'Escoge el ejercicio a añadir:',
      });

    // Seleccionamos el ejercicio y lo añadimos
    const correrButton = page.locator('button[name="Correr"]');
    await Promise.all([correrButton.click(), page.waitForNavigation()]);

    sleep(1);

    // Añadimos una serie al nuevo ejercicio
    const anyadirSerieButton = page.locator('div:nth-child(1) div button[name="anyadir_serie"]');
    await Promise.all([anyadirSerieButton.click(), page.waitForNavigation()]);

    sleep(1);

    // Rellenamos los datos de la serie
    const primer_input = page.locator('form div:nth-child(1) input[class="form-control"]');
    const segundo_input = page.locator('form div:nth-child(2) input[class="form-control"]');

    primer_input.type('12');
    segundo_input.type('12');

    // Guardamos la nueva serie del ejercicio
    const guardarSerieButton = page.locator('button[name="guardar_serie"]');
    await Promise.all([guardarSerieButton.click(), page.waitForNavigation()]);
    sleep(2);

    // Comprobamos que se ha sumado un nuevo ejercicio
    check(page, {
      'Se suma un nuevo ejercicio a la sesion': p => p.$$('table[name="tabla_ejercicio"]').length == numeroEjercicios+1  , 
    });

  } finally {
    page.close();
  }
}