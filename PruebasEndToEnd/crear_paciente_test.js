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

    // CREAMOS EL NUEVO PACIENTE

    // Localizamos el boton de añadir un nuevo paciente
    const addPacienteButton = page.locator('button[name="add"]');

    // Pulsamos el boton
    await Promise.all([page.waitForNavigation(), addPacienteButton.click()]);
 
    sleep(2);

    // Rellenamos los campos del nuevo paciente
    page.locator('input[name="dni"]').clear()
    page.locator('input[name="dni"]').type('2');
    page.locator('input[name="nombre"]').clear()
    page.locator('input[name="nombre"]').type('Luis');
    page.locator('input[name="edad"]').clear()
    page.locator('input[name="edad"]').type('18');
    page.locator('input[name="cita"]').clear()
    page.locator('input[name="cita"]').type('Revision');

    sleep(2);

    // Localizamos el boton de añadir el paciente
    const submitAddPacienteButton = page.locator('button[type="submit"]');

    // Pulsamos el boton
    await Promise.all([page.waitForNavigation(), submitAddPacienteButton.click()]);

    sleep(3);

    // Comprobamos que nos devuelve al listado de pacientes
    check(page, {
        'Redireccion al listado de pacientes (tras añadir el nuevo paciente)': p => p.locator('h2').textContent() == 'Listado de pacientes',
      });

    // Comprobamos que el medico se ha añadido correctamente (los campos son los correctos)
    let len = page.$$("table tbody tr").length;

    check(page, {
      'Nombre del nuevo paciente': p => p.$$("table tbody tr")[len-1].$('td[name="nombre"]').textContent().trim() == 'Luis',
      'DNI del nuevo paciente': p => parseInt(p.$$("table tbody tr")[len-1].$('td[name="dni"]').textContent()) == '2',
    });

  } finally {
    page.close();
  }
}