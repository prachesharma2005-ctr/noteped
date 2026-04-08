// humbly: this is the corrected JS for GitHub profile search

const urlBase = "https://api.github.com/users";

const searchInputEl = document.querySelector("#search"); // input box
const searchBtnEl = document.querySelector(".Primary_btn"); // search button
const profileContainerEl = document.querySelector(".profilecontainer"); // where profile HTML goes
const loadingEl = document.querySelector(".loading"); // messages / loading

// humbly: create profile HTML
const generateProfile = (profile) => {
  return `
    <div class="profile_box">
      <div class="top_section">
        <div class="left">
          <div class="avatar">
            <img src="${profile.avatar_url}" alt="avatar" />
          </div>
          <div class="self">
            <h1>${profile.name || "No Name"}</h1>
            <h2>@${profile.login}</h2>
          </div>
        </div>
        <div>
          <a href="${
            profile.html_url
          }" target="_blank" rel="noopener noreferrer">
            <button class="Primary_btn">Open on GitHub</button>
          </a>
        </div>
      </div>

      <div class="about">
        <h3>ABOUT</h3>
        <p>${profile.bio || "No bio available"}</p>
      </div>

      <div class="status">
        <div class="status-item">
          <h4>Followers</h4>
          <p>${profile.followers}</p>
        </div>
        <div class="status-item">
          <h4>Following</h4>
          <p>${profile.following}</p>
        </div>
        <div class="status-item">
          <h4>Repos</h4>
          <p>${profile.public_repos}</p>
        </div>
      </div>
    </div>
  `;
};

// humbly: fetch profile from GitHub API
const fetchProfile = async () => {
  const username = searchInputEl.value.trim();
  if (!username) {
    loadingEl.innerText = "Please enter a username";
    loadingEl.style.color = "red";
    profileContainerEl.innerHTML = "";
    return;
  }

  loadingEl.innerText = "Loading...";
  loadingEl.style.color = "black";
  profileContainerEl.innerHTML = "";

  try {
    const res = await fetch(`${urlBase}/${encodeURIComponent(username)}`);
    const data = await res.json();

    if (data.message) {
      // API returns { message: "Not Found" } on error
      loadingEl.innerText = data.message;
      loadingEl.style.color = "red";
      profileContainerEl.innerHTML = "";
    } else {
      loadingEl.innerText = "";
      profileContainerEl.innerHTML = generateProfile(data);
    }
  } catch (err) {
    // humbly: network or other error
    console.error(err);
    loadingEl.innerText = "Something went wrong. Check console.";
    loadingEl.style.color = "red";
    profileContainerEl.innerHTML = "";
  }
};

// humbly: wire up click + support Enter key
searchBtnEl.addEventListener("click", fetchProfile);
searchInputEl.addEventListener("keydown", (e) => {
  if (e.key === "Enter") fetchProfile();
});
