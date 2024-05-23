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

    // Escribimos la informacion del medico con el que haremos el login
    page.locator('input[name="nombre"]').clear()
    page.locator('input[name="DNI"]').clear()
    page.locator('input[name="nombre"]').type('Pedro');
    page.locator('input[name="DNI"]').type('1');
    sleep(1);

    // Pulsamos el boton de iniciar sesion
    await Promise.all([page.waitForNavigation(), submitButton.click()]);
    // Esperamos unos cinco segundos
    sleep(3);
    // Comprobamos que nos redirige al listado de pacientes del medico
    check(page, {
      'header': p => p.locator('h2').textContent() == 'Listado de pacientes',
    });

    // CREAMOS EL NUEVO PACIENTE
    const addPacienteButton = page.locator('button[name="add"]');
    await Promise.all([page.waitForNavigation(), addPacienteButton.click()]);
    sleep(2);
    page.locator('input[name="dni"]').clear()
    page.locator('input[name="dni"]').type('2');
    page.locator('input[name="nombre"]').clear()
    page.locator('input[name="nombre"]').type('Luis');
    page.locator('input[name="edad"]').clear()
    page.locator('input[name="edad"]').type('18');
    page.locator('input[name="cita"]').clear()
    page.locator('input[name="cita"]').type('Revision');

    const submitAddPacienteButton = page.locator('button[type="submit"]');
    await Promise.all([page.waitForNavigation(), submitAddPacienteButton.click()]);

    sleep(3);
    check(page, {
        'header': p => p.locator('h2').textContent() == 'Listado de pacientes',
      });
    

  } finally {
    page.close();
  }
}