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
    console.log(nombreSesion);

    const ultimaSesionButton = page.$('table tbody tr:last-child button[name="editar"]');
    await Promise.all([ultimaSesionButton.click(), page.waitForNavigation()]);

    sleep(2);

    // Comprobamos que estamos en la pagina de editar la sesion
    check(page, {
        'Redireccion a editar ultima sesion': p => p.locator('h1').textContent().trim().includes(nombreSesion),
      });

    // Comprobamos el numero de ejercicios que tiene la sesion
    const numeroEjercicios = 2;
    check(page, {
      'Numero de ejercicios de la sesion': p => p.$$('table[name="tabla_ejercicio"]').length == numeroEjercicios  , 
    });

  } finally {
    page.close();
  }
}