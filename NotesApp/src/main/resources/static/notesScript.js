function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}


const username = getCookie("username");

if (username) {
    document.getElementById("username").textContent = username;
} else {
    document.getElementById("username").textContent = "Username: Guest";
}

const settingsTab = document.getElementById("settingsTab");
const arrow = document.getElementById("arrow");

// Переключение видимости вкладки и анимация стрелки
function toggleSettingsTab() {
    if (settingsTab.style.opacity === "1") {
        settingsTab.style.opacity = "0";
        settingsTab.style.pointerEvents = "none"; // Делаем вкладку неактивной во время анимации скрытия
        arrow.style.transform = "rotate(0)";
    } else {
        settingsTab.style.display = "block"; // Показываем вкладку перед анимацией появления
        settingsTab.style.pointerEvents = "auto"; // Восстанавливаем активность вкладки
        settingsTab.style.opacity = "1";

        // Позиционируем вкладку прямо под стрелочкой
        const logoContainerRect = document.querySelector(".logo-container").getBoundingClientRect();
        settingsTab.style.left = logoContainerRect.left + "px";
        settingsTab.style.top = logoContainerRect.bottom + "px";

        arrow.style.transform = "rotate(180deg)";
    }
}

// Обработка клика по лого
function handleLogoClick(event) {
    event.stopPropagation();
    toggleSettingsTab();
}

// Обработка клика по имени пользователя
function handleUsernameClick(event) {
    event.stopPropagation();
    toggleSettingsTab();
}

function logout() {
    document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; //по другому никак(
    window.location.href = "/";
}

const profileWindow = document.getElementById("profileWindow");

function openProfile() {
    closeSettingsTab();
    closeBigSettingsWindow();



    // более сложная версия то что внизу разкоментить

    // const profileUsername = document.getElementById('profileUsername').getAttribute('data-username');
    // const profileEmail = document.getElementById('profileEmail').getAttribute('data-email');
    //
    // document.getElementById('profileUsername').textContent = profileUsername;
    // document.getElementById('profileEmail').textContent = profileEmail;

    //    <p><strong>Username:</strong> <span id="profileUsername" th:attr="data-username=${profileUsername}"></span></p>
    //     <p><strong>Email:</strong> <span id="profileEmail" th:attr="data-email=${profileEmail}"></span></p>

    // а это добавить в хтмл



    profileWindow.style.display = "block";
    setTimeout(() => {
        profileWindow.style.transform = "translate(-50%, -50%) scale(1)";
        profileWindow.style.opacity = 1;
    }, 0);
}




function closeProfile() {
    profileWindow.style.transform = "translate(-50%, -50%) scale(0.5)";

    requestAnimationFrame(() => {
        profileWindow.style.opacity = 0; // Устанавливаем прозрачность на 0
    });

    setTimeout(() => {
        profileWindow.style.display = "none";
    }, 300);
}


const bigSettingsWindow = document.getElementById("bigSettingsWindow");

// Открытие большого окна настроек
function openBigSettingsWindow() {
    closeSettingsTab(); // Закрыть маленькое окно настроек
    closeProfile();
    bigSettingsWindow.style.display = "block";
    arrow.style.transform = "rotate(0)"; // Перевернуть стрелочку обратно
    setTimeout(() => {
        bigSettingsWindow.style.transform = "translate(-50%, -50%) scale(1)";
        bigSettingsWindow.style.opacity = 1;
    }, 0);
}

// Закрытие большого окна настроек
function closeBigSettingsWindow() {
    bigSettingsWindow.style.transform = "translate(-50%, -50%) scale(0.5)";

    requestAnimationFrame(() => {
        bigSettingsWindow.style.opacity = 0; // Устанавливаем прозрачность на 0
    });

    setTimeout(() => {
        bigSettingsWindow.style.display = "none";
    }, 300);
}

// Закрытие маленького окна настроек
function closeSettingsTab() {
    settingsTab.style.opacity = "0";
    settingsTab.style.pointerEvents = "none";
}

// Время
const dateElement = document.querySelector('.coordinate-date');

// Получаем текущую дату
const currentDate = new Date();

// Настройка локали для отображения дня недели и месяца на английском
const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };

// Форматируем дату и вставляем в элемент
dateElement.textContent = currentDate.toLocaleDateString('en-US', options);

// Блюр бг
function applyBlur() {
    const blurValue = document.getElementById('blur-slider').value;
    const backgroundContainer = document.querySelector('.background-container');
    backgroundContainer.style.filter = `blur(${blurValue}px)`;
}

let noteNullText = '';
let noteText;

const notePopup = document.getElementById('notePopup');

function openNotePopup() {
    notePopup.classList.add('show');
    const textarea = notePopup.querySelector('textarea');
    textarea.value = noteNullText;
}


function closeNotePopup() {
    const textarea = notePopup.querySelector('textarea');
    noteText = textarea.value; // Сохраняем текст заметки
    notePopup.classList.remove('show');
}


function saveNote() {
    const content = document.getElementById("noteContent").value;

    fetch("/notes", {
        method: "POST",
        body: JSON.stringify({ content }), // Отправляем данные в формате JSON
        headers: {
            "Content-Type": "application/json" // Указываем, что отправляем JSON
        }
    })
        .then(response => {
            if (response.ok) {
                console.log("Заметка успешно сохранена!");
                closeNotePopup();
            } else {
                console.error("Произошла ошибка при сохранении заметки.");
            }
        })
        .catch(error => {
            console.error("Произошла ошибка при отправке запроса:", error);
        });
}


function saveSelectedLogos(avatarUrl) {
    fetch("/notes", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            selectedAvatar: avatarUrl
        })
    })
        .then(response => response.json())
        .then(data => {
            // Данные успешно сохранены
        })
        .catch(error => {
            console.error("Error saving images:", error);
        });
}

function saveSelectedBackgrounds(backgroundUrl) {
    // Отправляем данные на сервер
    fetch("/notes", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            selectedBackground: backgroundUrl
        })
    })
        .then(response => response.json())
        .then(data => {
            // Данные успешно сохранены
        })
        .catch(error => {
            console.error("Error saving images:", error);
        });
}

function selectAvatar(element) {
    const selectedAvatar = element.src;
    const avatarImage = document.querySelector(".logo");
    avatarImage.style.backgroundImage = `url('${selectedAvatar}')`;
    saveSelectedLogos(selectedAvatar, null); // Вызываем функцию для сохранения
}

function selectBackground(element) {
    const selectedBackground = element.src;
    const backgroundContainer = document.querySelector(".background-container");
    backgroundContainer.style.backgroundImage = `url('${selectedBackground}')`;
    saveSelectedBackgrounds(selectedBackground); // Вызываем функцию для сохранения
}






// Закрываем вкладку при клике в любое место на экране
window.addEventListener("click", function (event) {
    if (!settingsTab.contains(event.target) && !arrow.contains(event.target)) {
        settingsTab.style.opacity = "0";
        settingsTab.style.pointerEvents = "none"; // Делаем вкладку неактивной во время анимации скрытия
        arrow.style.transform = "rotate(0)";
    }
});