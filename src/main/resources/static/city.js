const canvas = document.getElementById("cityCanvas");
const ctx = canvas.getContext("2d");
const tileInfo = document.getElementById("tileInfo");

const TILE_SIZE = 20;

const TILE_COLORS = {
    ROAD: "#758397",
    RESIDENTIAL: "#76B99A",
    COMMERCIAL: "#E9B866",
    INDUSTRIAL: "#B39AD9"
};

let currentCity = null;

let scale = 1;
let offsetX = 0;
let offsetY = 0;

let dragging = false;
let lastPointerX = 0;
let lastPointerY = 0;

// ====================
// CITY GENERATION
// ====================

async function loadCity() {
    const width = Number(document.getElementById("cityWidth").value);
    const height = Number(document.getElementById("cityHeight").value);
    const seed = Number(document.getElementById("citySeed").value);

    const roadSpacing = Number(document.getElementById("roadSpacing").value);
    const industrialZoneSize = Number(document.getElementById("industrialZoneSize").value);
    const commercialRadius = Number(document.getElementById("commercialRadius").value);

    if (
        !Number.isInteger(width) || width < 1 || width > 100 ||
        !Number.isInteger(height) || height < 1 || height > 100 ||
        !Number.isSafeInteger(seed) ||
        !Number.isInteger(roadSpacing) || roadSpacing < 1 || roadSpacing > 100 ||
        !Number.isInteger(industrialZoneSize) || industrialZoneSize < 1 || industrialZoneSize > 50 ||
        !Number.isInteger(commercialRadius) || commercialRadius < 0 || commercialRadius > 100
    ) {
        alert("Enter valid city generation parameters.");
        return;
    }

    const params = new URLSearchParams({
        width: String(width),
        height: String(height),
        seed: String(seed),
        roadSpacing: String(roadSpacing),
        industrialZoneSize: String(industrialZoneSize),
        commercialRadius: String(commercialRadius)
    });

    const button = document.getElementById("generateButton");
    button.disabled = true;

    try {
        const response = await fetch(
            `/api/v1/cities/generate?${params}`
        );

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        const city = await response.json();

        drawCity(city);
        updateStats(city.stats);
    } catch (error) {
        console.error("Failed to generate city:", error);
        alert("City generation failed. Check the browser console.");
    } finally {
        button.disabled = false;
    }
}

// ====================
// STATISTICS
// ====================

function updateStats(stats) {
    document.getElementById("roadsCount").textContent = stats.roads;
    document.getElementById("residentialCount").textContent = stats.residential;
    document.getElementById("commercialCount").textContent = stats.commercial;
    document.getElementById("industrialCount").textContent = stats.industrial;
}

// ====================
// CANVAS RENDERING
// ====================

function resizeCanvas() {
    const rect = canvas.getBoundingClientRect();
    const pixelRatio = window.devicePixelRatio || 1;

    canvas.width = Math.round(rect.width * pixelRatio);
    canvas.height = Math.round(rect.height * pixelRatio);

    ctx.setTransform(pixelRatio, 0, 0, pixelRatio, 0, 0);

    render();
}

function drawCity(city) {
    currentCity = city;

    scale = 1;
    offsetX = 0;
    offsetY = 0;

    tileInfo.textContent = "Hover over a tile";

    render();
}

function render() {
    const width = canvas.clientWidth;
    const height = canvas.clientHeight;

    ctx.clearRect(0, 0, width, height);

    if (!currentCity) return;

    ctx.save();

    ctx.translate(offsetX, offsetY);
    ctx.scale(scale, scale);

    for (const tile of currentCity.tiles) {
        ctx.fillStyle = TILE_COLORS[tile.type] ?? "#FF00FF";

        ctx.fillRect(
            tile.x * TILE_SIZE,
            tile.y * TILE_SIZE,
            TILE_SIZE - 1,
            TILE_SIZE - 1
        );
    }

    ctx.restore();
}

// ====================
// ZOOM
// ====================

canvas.addEventListener("wheel", event => {
    event.preventDefault();

    const rect = canvas.getBoundingClientRect();

    const mouseX = event.clientX - rect.left;
    const mouseY = event.clientY - rect.top;

    const oldScale = scale;
    const factor = event.deltaY < 0 ? 1.1 : 0.9;

    scale = Math.max(0.3, Math.min(5, scale * factor));

    offsetX = mouseX - (mouseX - offsetX) * (scale / oldScale);
    offsetY = mouseY - (mouseY - offsetY) * (scale / oldScale);

    render();
}, {passive: false});

// ====================
// DRAG & PAN
// ====================

canvas.addEventListener("pointerdown", event => {
    if (event.button !== 0) return;

    dragging = true;

    lastPointerX = event.clientX;
    lastPointerY = event.clientY;

    canvas.setPointerCapture(event.pointerId);
});

canvas.addEventListener("pointermove", event => {
    if (dragging) {
        offsetX += event.clientX - lastPointerX;
        offsetY += event.clientY - lastPointerY;

        lastPointerX = event.clientX;
        lastPointerY = event.clientY;

        render();
        return;
    }

    inspectTile(event);
});

function stopDragging() {
    dragging = false;
}

canvas.addEventListener("pointerup", stopDragging);
canvas.addEventListener("pointercancel", stopDragging);

canvas.addEventListener("pointerleave", () => {
    if (!dragging) {
        tileInfo.textContent = "Hover over a tile";
    }
});

// ====================
// TILE INSPECTOR
// ====================

function inspectTile(event) {
    if (!currentCity) return;

    const rect = canvas.getBoundingClientRect();

    const x = Math.floor(
        (event.clientX - rect.left - offsetX) / (TILE_SIZE * scale)
    );

    const y = Math.floor(
        (event.clientY - rect.top - offsetY) / (TILE_SIZE * scale)
    );

    if (
        x < 0 || y < 0 ||
        x >= currentCity.width ||
        y >= currentCity.height
    ) {
        tileInfo.textContent = "Outside city";
        return;
    }

    const tile = currentCity.tiles.find(
        tile => tile.x === x && tile.y === y
    );

    tileInfo.textContent = tile
        ? `${tile.type} · X: ${x}, Y: ${y}`
        : "No tile";
}

// ====================
// INITIALIZATION
// ====================

document
    .getElementById("generateButton")
    .addEventListener("click", loadCity);

window.addEventListener("resize", resizeCanvas);

resizeCanvas();
loadCity();

document.getElementById("resetViewButton")
    .addEventListener("click", () => {
        scale = 1;
        offsetX = 0;
        offsetY = 0;
        render();
    });