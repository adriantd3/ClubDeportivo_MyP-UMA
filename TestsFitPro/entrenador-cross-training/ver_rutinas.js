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

    // Login del entrenador
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

    // Comprobamos que estamos en la pagina de rutinas del entrenador
    check(page, {
        'Redireccion a página rutinas': p => p.locator('h1').textContent() == 'Rutinas',
      });
    
    // Obtenemos el numero de rutinas propias del entrenador
    const numeroDeRutinas = 4;
    check(page, {
        'Numero de rutinas' : p => p.$$("table tbody tr").length == numeroDeRutinas
    })
    sleep(2);

  } finally {
    page.close();
  }
}