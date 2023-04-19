const form = document.querySelector('#form');

form.addEventListener('submit', async (event) => {
  event.preventDefault();

  const formData = new FormData(form);
  const data = {
    name: formData.get('name'),
    age: formData.get('age'),
    email: formData.get('email'),