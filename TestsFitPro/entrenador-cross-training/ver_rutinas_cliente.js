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

    sleep(2);

    //Localizamos el boton de clientes
    const clientesButton = page.locator('button[name="clientes"]');

    // Pulsamos el boton de clientes
    await Promise.all([clientesButton.click(), page.waitForNavigation()]);

    // Comprobamos que estamos en la pagina de clientes
    check(page, {
        'Redireccion a página clientes': p => p.locator('h1').textContent() == 'Clientes',
      });
    
    sleep(2);

    // Localizamos el boton para ver las rutinas del primer cliente
    const rutinasClienteButton = page.$('body > section > section > table > tbody > tr:nth-child(1) > td:nth-child(3) > button');
    await Promise.all([rutinasClienteButton.click(), page.waitForNavigation()]);

    sleep(2);
    
    check(page, {
        'Redireccion a rutinas del cliente': p => p.locator('h1').textContent() == 'Rutinas de Alvaro',
      });

    // Comprobamos que este cliente tiene dos rutinas asignadas con nombres 'Prueba' y 'Rutina 1' 
    const numeroDeRutinas = 2;
    check(page, {
        'Numero de rutinas asignadas al cliente' : p => p.$$("table tbody tr").length == numeroDeRutinas
    })

    /*
    check(page, {
        'Primera rutina': p => p.$$("table tbody tr")[numeroDeRutinas-2].$('td[name="nombre"]').textContent().trim() == 'Rutina 1',
        'Segunda rutina': p => p.$$("table tbody tr")[numeroDeRutinas-1].$('td[name="nombre"]').textContent().trim() == 'PRUEBA',
      });*/

  } finally {
    page.close();
  }
}