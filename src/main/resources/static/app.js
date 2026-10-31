document.querySelectorAll('[data-validate]').forEach(form => form.addEventListener('submit', event => {
  const password = form.querySelector('input[name=password]');
  if (password && password.value.length < 6) { event.preventDefault(); alert('Please use a password with at least six characters.'); }
}));

const search = document.querySelector('#topic-search');
if (search) search.addEventListener('input', () => {
  const query = search.value.trim().toLowerCase();
  document.querySelectorAll('[data-topic]').forEach(topic => topic.hidden = query !== '' && !topic.textContent.toLowerCase().includes(query));
});
