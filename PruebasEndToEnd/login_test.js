//Realizado por Adrián Torremocha Doblas y Ezequiel Sánchez García
import { browser } from 'k6/experimental/browser';
import { check, sleep } from 'k6';


export const options = {
  scenarios: {
    ui: {
      executor: 'shared-iterations', // para realizar iteraciones sin indicar el tiempo
      options: {
        browser: {
          type: 'chromium',
        }
      }
    }
  },
  thresholds: {
    checks: ["rate==1.0"]
  }
}

export default async function () {
  const page = browser.newPage();
  try {
    await page.goto('http://localhost:4200/');

    // Localizamos el boton de iniciar sesion
    const submitButton = page.locator('button[name="login"]');

    sleep(2);

    // Escribimos la informacion del medico con el que haremos el login
    page.locator('input[name="nombre"]').clear()
    page.locator('input[name="DNI"]').clear()
    page.locator('input[name="nombre"]').type('Pedro');
    page.locator('input[name="DNI"]').type('1');
    
    sleep(2);

    // Pulsamos el boton de iniciar sesion
    await Promise.all([page.waitForNavigation(), submitButton.click()]);
    
    sleep(3);
    
    // Comprobamos que nos redirige al listado de pacientes del medico
    check(page, {
      'Redireccion al listado de pacientes (tras hacer el login)': p => p.locator('h2').textContent() == 'Listado de pacientes',
    });

  } finally {
    page.close();
  }
}