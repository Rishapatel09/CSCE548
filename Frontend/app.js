function baseUrl() {
  return localStorage.getItem("baseUrl") || "http://localhost:8080";
}

function setStatus(msg, kind = "ok") {
  const status = document.getElementById("status");
  const dot = document.getElementById("dot");
  status.textContent = msg;

  dot.classList.remove("ok", "err");
  if (kind === "ok") dot.classList.add("ok");
  if (kind === "err") dot.classList.add("err");
}

async function getJson(url) {
  setStatus(`Calling: ${url}`, "ok");
  const res = await fetch(url, { headers: { Accept: "application/json" } });
  if (!res.ok) {
    const t = await res.text().catch(() => "");
    throw new Error(`HTTP ${res.status} ${res.statusText}\n${t}`);
  }
  const ct = res.headers.get("content-type") || "";
  if (ct.includes("application/json")) return res.json();
  return res.text();
}

function pretty(el, data) {
  el.textContent = typeof data === "string" ? data : JSON.stringify(data, null, 2);
}

function numValue(id) {
  const v = document.getElementById(id).value.trim();
  return v === "" ? null : Number(v);
}

function safeDate(s) {
  if (!s) return "";
  return String(s).replace("T", " ").slice(0, 19);
}

function clearTable(tableId) {
  document.querySelector(`#${tableId} tbody`).innerHTML = "";
}

function addRow(tableId, cells) {
  const tr = document.createElement("tr");
  for (const c of cells) {
    const td = document.createElement("td");
    td.textContent = c ?? "";
    tr.appendChild(td);
  }
  document.querySelector(`#${tableId} tbody`).appendChild(tr);
}

function setTab(tabName) {
  document.querySelectorAll(".tab").forEach(t => {
    t.classList.toggle("active", t.dataset.tab === tabName);
  });
  document.querySelectorAll(".panel").forEach(p => p.classList.remove("active"));
  document.getElementById(`panel-${tabName}`).classList.add("active");
}

window.addEventListener("DOMContentLoaded", () => {
  // Base URL
  document.getElementById("baseUrl").value = baseUrl();
  document.getElementById("saveBaseUrl").addEventListener("click", () => {
    const v = document.getElementById("baseUrl").value.trim();
    localStorage.setItem("baseUrl", v);
    setStatus(`Saved base URL = ${v}`, "ok");
  });

  // Theme
  const savedTheme = localStorage.getItem("theme") || "dark";
  if (savedTheme === "light") document.body.classList.add("light");
  document.getElementById("themeToggle").addEventListener("click", () => {
    document.body.classList.toggle("light");
    localStorage.setItem("theme", document.body.classList.contains("light") ? "light" : "dark");
  });

  // Tabs
  document.querySelectorAll(".tab").forEach(btn => {
    btn.addEventListener("click", () => setTab(btn.dataset.tab));
  });


  // USERS
  document.getElementById("usersAll").addEventListener("click", async () => {
    setTab("users");
    clearTable("usersTable");
    const raw = document.getElementById("usersRaw");
    try {
      const data = await getJson(`${baseUrl()}/users`);
      pretty(raw, data);

      if (Array.isArray(data)) {
        data.forEach(u => addRow("usersTable", [
          u.userId ?? u.user_id,
          u.name,
          u.email,
          safeDate(u.createdAt ?? u.created_at)
        ]));
      }
      setStatus("Loaded users (all).", "ok");
    } catch (e) {
      pretty(raw, String(e));
      setStatus("Users (all) failed.", "err");
    }
  });

  document.getElementById("usersOne").addEventListener("click", async () => {
    setTab("users");
    clearTable("usersTable");
    const raw = document.getElementById("usersRaw");
    const id = numValue("userId");
    if (!id) return setStatus("Enter user_id first.", "err");

    try {
      const data = await getJson(`${baseUrl()}/users/${id}`);
      pretty(raw, data);

      if (data && typeof data === "object") {
        addRow("usersTable", [
          data.userId ?? data.user_id,
          data.name,
          data.email,
          safeDate(data.createdAt ?? data.created_at)
        ]);
      }
      setStatus("Loaded user (single).", "ok");
    } catch (e) {
      pretty(raw, String(e));
      setStatus("User (single) failed.", "err");
    }
  });

  // ENTRIES
  document.getElementById("entriesAll").addEventListener("click", async () => {
    setTab("entries");
    clearTable("entriesTable");
    const raw = document.getElementById("entriesRaw");
    try {
      const data = await getJson(`${baseUrl()}/entries`);
      pretty(raw, data);

      if (Array.isArray(data)) {
        data.forEach(en => addRow("entriesTable", [
          en.entryId ?? en.entry_id,
          en.userId ?? en.user_id,
          en.entryDate ?? en.entry_date,
          (en.entryTime ?? en.entry_time ?? "").toString().slice(0, 8),
          en.mealType ?? en.meal_type,
          en.totalCalories ?? en.total_calories,
          en.totalProtein ?? en.total_protein,
          en.totalCarbs ?? en.total_carbs,
          en.totalFats ?? en.total_fats,
          en.notes ?? ""
        ]));
      }
      setStatus("Loaded entries (all).", "ok");
    } catch (e) {
      pretty(raw, String(e));
      setStatus("Entries (all) failed.", "err");
    }
  });

  document.getElementById("entriesOne").addEventListener("click", async () => {
    setTab("entries");
    clearTable("entriesTable");
    const raw = document.getElementById("entriesRaw");
    const id = numValue("entryId");
    if (!id) return setStatus("Enter entry_id first.", "err");

    try {
      const en = await getJson(`${baseUrl()}/entries/${id}`);
      pretty(raw, en);

      if (en && typeof en === "object") {
        addRow("entriesTable", [
          en.entryId ?? en.entry_id,
          en.userId ?? en.user_id,
          en.entryDate ?? en.entry_date,
          (en.entryTime ?? en.entry_time ?? "").toString().slice(0, 8),
          en.mealType ?? en.meal_type,
          en.totalCalories ?? en.total_calories,
          en.totalProtein ?? en.total_protein,
          en.totalCarbs ?? en.total_carbs,
          en.totalFats ?? en.total_fats,
          en.notes ?? ""
        ]);
      }
      setStatus("Loaded entry (single).", "ok");
    } catch (e) {
      pretty(raw, String(e));
      setStatus("Entry (single) failed.", "err");
    }
  });

  document.getElementById("entriesSubset").addEventListener("click", async () => {
    setTab("entries");
    clearTable("entriesTable");
    const raw = document.getElementById("entriesRaw");
    const userId = numValue("entryUserId");
    if (!userId) return setStatus("Enter user_id first.", "err");

    try {
      const data = await getJson(`${baseUrl()}/users/${userId}/entries`);
      pretty(raw, data);

      if (Array.isArray(data)) {
        data.forEach(en => addRow("entriesTable", [
          en.entryId ?? en.entry_id,
          en.userId ?? en.user_id,
          en.entryDate ?? en.entry_date,
          (en.entryTime ?? en.entry_time ?? "").toString().slice(0, 8),
          en.mealType ?? en.meal_type,
          en.totalCalories ?? en.total_calories,
          en.totalProtein ?? en.total_protein,
          en.totalCarbs ?? en.total_carbs,
          en.totalFats ?? en.total_fats,
          en.notes ?? ""
        ]));
      }
      setStatus("Loaded entries subset by user.", "ok");
    } catch (e) {
      pretty(raw, String(e));
      setStatus("Entries subset failed.", "err");
    }
  });

  // FOODS
  document.getElementById("foodsAll").addEventListener("click", async () => {
    setTab("foods");
    clearTable("foodsTable");
    const raw = document.getElementById("foodsRaw");
    try {
      const data = await getJson(`${baseUrl()}/food-items`);
      pretty(raw, data);

      if (Array.isArray(data)) {
        data.forEach(f => addRow("foodsTable", [
          f.foodId ?? f.food_id,
          f.entryId ?? f.entry_id,
          f.name,
          f.servingSize ?? f.serving_size,
          f.caloriesPerServing ?? f.calories_per_serving,
          f.protein,
          f.carbs,
          f.fats,
          f.servings,
          f.notes ?? ""
        ]));
      }
      setStatus("Loaded food items (all).", "ok");
    } catch (e) {
      pretty(raw, String(e));
      setStatus("Food items (all) failed.", "err");
    }
  });

  document.getElementById("foodsOne").addEventListener("click", async () => {
    setTab("foods");
    clearTable("foodsTable");
    const raw = document.getElementById("foodsRaw");
    const id = numValue("foodId");
    if (!id) return setStatus("Enter food_id first.", "err");

    try {
      const f = await getJson(`${baseUrl()}/food-items/${id}`);
      pretty(raw, f);

      if (f && typeof f === "object") {
        addRow("foodsTable", [
          f.foodId ?? f.food_id,
          f.entryId ?? f.entry_id,
          f.name,
          f.servingSize ?? f.serving_size,
          f.caloriesPerServing ?? f.calories_per_serving,
          f.protein,
          f.carbs,
          f.fats,
          f.servings,
          f.notes ?? ""
        ]);
      }
      setStatus("Loaded food item (single).", "ok");
    } catch (e) {
      pretty(raw, String(e));
      setStatus("Food item (single) failed.", "err");
    }
  });

  document.getElementById("foodsSubset").addEventListener("click", async () => {
    setTab("foods");
    clearTable("foodsTable");
    const raw = document.getElementById("foodsRaw");
    const entryId = numValue("foodEntryId");
    if (!entryId) return setStatus("Enter entry_id first.", "err");

    try {
      const data = await getJson(`${baseUrl()}/food-items/entry/${entryId}`);
      pretty(raw, data);

      if (Array.isArray(data)) {
        data.forEach(f => addRow("foodsTable", [
          f.foodId ?? f.food_id,
          f.entryId ?? f.entry_id,
          f.name,
          f.servingSize ?? f.serving_size,
          f.caloriesPerServing ?? f.calories_per_serving,
          f.protein,
          f.carbs,
          f.fats,
          f.servings,
          f.notes ?? ""
        ]));
      }
      setStatus("Loaded food items subset by entry.", "ok");
    } catch (e) {
      pretty(raw, String(e));
      setStatus("Food subset failed.", "err");
    }
  });

  // Fetch all + clear
  document.getElementById("fetchAll").addEventListener("click", async () => {
    await document.getElementById("usersAll").click();
    await document.getElementById("entriesAll").click();
    await document.getElementById("foodsAll").click();
    setStatus("Fetched all tables.", "ok");
  });

  document.getElementById("clearAll").addEventListener("click", () => {
    clearTable("usersTable"); document.getElementById("usersRaw").textContent = "";
    clearTable("entriesTable"); document.getElementById("entriesRaw").textContent = "";
    clearTable("foodsTable"); document.getElementById("foodsRaw").textContent = "";
    setStatus("Cleared.", "ok");
  });

  setStatus("Ready.", "ok");
});