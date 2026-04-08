const productList = document.getElementById('productList');
const searchInput = document.getElementById('searchInput');
const categoryFilter = document.getElementById('categoryFilter');

let products = []; 

async function fetchProducts() {
  const response = await fetch('https://fakestoreapi.com/products');
  const data = await response.json();
  products = data;
  populateCategoryFilter();
  displayProducts(products);
}

function displayProducts(filteredProducts) {
  productList.innerHTML = '';

  if (filteredProducts.length === 0) {
    productList.innerHTML = '<p>No products found.</p>';
    return;
  }

  filteredProducts.forEach(product => {
    const productCard = document.createElement('div');
    productCard.className = 'product';
    productCard.innerHTML = `
      <img src="${product.image}" alt="${product.title}" />
      <h3>${product.title}</h3>
      <p>$${product.price.toFixed(2)}</p>
      <p><small>${product.category}</small></p>
    `;
    productList.appendChild(productCard);
  });
}

function populateCategoryFilter() {
  const categories = [...new Set(products.map(p => p.category))];
  categories.forEach(cat => {
    const option = document.createElement('option');
    option.value = cat;
    option.textContent = cat;
    categoryFilter.appendChild(option);
  });
}

function filterProducts() {
  const searchText = searchInput.value.toLowerCase();
  const selectedCategory = categoryFilter.value;

  const filtered = products.filter(product => {
    const matchesSearch = product.title.toLowerCase().includes(searchText);
    const matchesCategory = selectedCategory ? product.category === selectedCategory : true;
    return matchesSearch && matchesCategory;
  });

  displayProducts(filtered);
}

searchInput.addEventListener('input', filterProducts);
categoryFilter.addEventListener('change', filterProducts);

fetchProducts();
