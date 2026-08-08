async function submitRegister() {
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const message = document.getElementById('registerMessage');
    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, password })
        });
        const result = await response.json();
        if (response.ok) {
            message.className = 'text-success';
            message.textContent = result.message;
            setTimeout(() => window.location.href = '/login', 1200);
        } else {
            message.className = 'text-danger';
            message.textContent = result.error || JSON.stringify(result);
        }
    } catch (error) {
        message.className = 'text-danger';
        message.textContent = 'Unable to register. Try again later.';
    }
}

async function submitWeatherUpload() {
    const fileInput = document.getElementById('weatherFile');
    const message = document.getElementById('uploadMessage');
    const file = fileInput.files[0];
    if (!file) {
        message.className = 'text-danger';
        message.textContent = 'Please select a CSV file to upload.';
        return;
    }
    try {
        const formData = new FormData();
        formData.append('file', file);
        const response = await fetch('/api/weather/upload', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        if (response.ok) {
            message.className = 'text-success';
            message.textContent = `Uploaded ${result.length} weather records successfully.`;
            if (document.querySelector('#weatherTable')) {
                populateWeatherRows(result);
            }
        } else {
            message.className = 'text-danger';
            message.textContent = result.error || JSON.stringify(result);
        }
    } catch (error) {
        message.className = 'text-danger';
        message.textContent = 'Upload failed. Check the file format and try again.';
    }
}

async function loadWeatherRecords() {
    try {
        const response = await fetch('/api/weather/history');
        if (!response.ok) {
            return;
        }
        const data = await response.json();
        populateWeatherRows(data);
    } catch (error) {
        console.error(error);
    }
}

function populateWeatherRows(records) {
    const tbody = document.querySelector('#weatherTable tbody');
    if (!tbody) return;
    tbody.innerHTML = '';
    records.slice(-10).reverse().forEach(record => {
        const row = `<tr>
            <td>${record.date}</td>
            <td>${record.temperature}</td>
            <td>${record.humidity}</td>
            <td>${record.pressure}</td>
            <td>${record.weatherCondition}</td>
        </tr>`;
        tbody.insertAdjacentHTML('beforeend', row);
    });
}

async function submitPrediction() {
    const request = {
        temperature: parseFloat(document.getElementById('temperature').value),
        humidity: parseFloat(document.getElementById('humidity').value),
        pressure: parseFloat(document.getElementById('pressure').value),
        windSpeed: parseFloat(document.getElementById('windSpeed').value),
        cloudCover: parseFloat(document.getElementById('cloudCover').value),
        precipitation: parseFloat(document.getElementById('precipitation').value)
    };
    try {
        const response = await fetch('/api/prediction/predict', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(request)
        });
        const result = await response.json();
        if (response.ok) {
            document.getElementById('decisionResult').textContent = result.decisionTreeResult;
            document.getElementById('regressionResult').textContent = `${result.regressionResult.toFixed(1)} °C`;
            document.getElementById('predictionExplanation').textContent = result.explanation;
        } else {
            document.getElementById('predictionExplanation').textContent = result.error || 'Prediction failed.';
        }
    } catch (error) {
        document.getElementById('predictionExplanation').textContent = 'Prediction failed. Please try again.';
    }
}

async function loadPredictionHistory() {
    try {
        const response = await fetch('/api/prediction/history');
        const records = await response.json();
        const tbody = document.querySelector('#historyTable tbody');
        if (!tbody) return;
        tbody.innerHTML = '';
        records.forEach(item => {
            const row = `<tr>
                <td>${new Date(item.predictionDate).toLocaleString()}</td>
                <td>${item.temperature}</td>
                <td>${item.humidity}</td>
                <td>${item.pressure}</td>
                <td>${item.decisionTreePrediction}</td>
                <td>${item.regressionPrediction.toFixed(1)} °C</td>
            </tr>`;
            tbody.insertAdjacentHTML('beforeend', row);
        });
    } catch (error) {
        console.error(error);
    }
}

async function loadModelPerformance() {
    try {
        const response = await fetch('/api/model/performance');
        if (!response.ok) {
            return;
        }
        const metrics = await response.json();
        const list = document.getElementById('performanceMetrics');
        if (list) {
            list.innerHTML = `
                <li class="list-group-item">Decision Tree Accuracy: ${formatPercent(metrics.decisionTreeAccuracy)}</li>
                <li class="list-group-item">Precision: ${formatPercent(metrics.precision)}</li>
                <li class="list-group-item">Recall: ${formatPercent(metrics.recall)}</li>
                <li class="list-group-item">F1 Score: ${formatPercent(metrics.f1Score)}</li>
                <li class="list-group-item">MAE: ${metrics.mae.toFixed(2)}</li>
                <li class="list-group-item">MSE: ${metrics.mse.toFixed(2)}</li>
                <li class="list-group-item">RMSE: ${metrics.rmse.toFixed(2)}</li>
                <li class="list-group-item">R² Score: ${metrics.r2.toFixed(2)}</li>
            `;
        }
        if (document.getElementById('performanceChart')) {
            renderPerformanceChart(metrics);
        }
        if (document.getElementById('accuracyChart')) {
            renderAccuracyChart(metrics);
        }
        if (document.getElementById('performanceSummary')) {
            document.getElementById('performanceSummary').textContent =
                `Decision tree accuracy is ${formatPercent(metrics.decisionTreeAccuracy)} and regression RMSE is ${metrics.rmse.toFixed(2)}.`;
        }
    } catch (error) {
        console.error(error);
    }
}

function formatPercent(value) {
    return `${(value * 100).toFixed(1)}%`;
}

function renderPerformanceChart(metrics) {
    const ctx = document.getElementById('performanceChart');
    if (!ctx) return;
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Accuracy', 'Precision', 'Recall', 'F1 Score'],
            datasets: [{
                label: 'Decision Tree',
                data: [metrics.decisionTreeAccuracy, metrics.precision, metrics.recall, metrics.f1Score],
                backgroundColor: 'rgba(54, 162, 235, 0.7)'
            }]
        },
        options: {
            scales: {
                y: { beginAtZero: true, max: 1 }
            }
        }
    });
}

function renderAccuracyChart(metrics) {
    const ctx = document.getElementById('accuracyChart');
    if (!ctx) return;
    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Accuracy', 'Error'],
            datasets: [{
                data: [metrics.decisionTreeAccuracy, 1 - metrics.decisionTreeAccuracy],
                backgroundColor: ['rgba(75, 192, 192, 0.7)', 'rgba(255, 99, 132, 0.6)']
            }]
        },
        options: {
            plugins: {
                tooltip: {
                    callbacks: {
                        label(context) {
                            return `${context.label}: ${(context.parsed * 100).toFixed(1)}%`;
                        }
                    }
                }
            }
        }
    });
}

async function loadDashboard() {
    if (document.body.dataset.page !== 'dashboard') return;
    await loadModelPerformance();
    try {
        const response = await fetch('/api/weather/history');
        const weatherRecords = await response.json();
        const summary = document.getElementById('weatherSummary');
        if (summary) {
            summary.innerHTML = '';
            weatherRecords.slice(-5).reverse().forEach(record => {
                const item = document.createElement('li');
                item.className = 'list-group-item';
                item.textContent = `${record.date}: ${record.temperature}°C, ${record.weatherCondition}`;
                summary.appendChild(item);
            });
        }
    } catch (error) {
        console.error(error);
    }
}

async function handleAgentSubmit(event) {
    if (event) event.preventDefault();
    const input = document.getElementById('agentInput');
    const sendBtn = document.getElementById('sendBtn');
    if (!input || !input.value.trim()) return;

    const userMessage = input.value.trim();
    input.value = '';
    appendAgentMessage(userMessage, true);

    if (sendBtn) sendBtn.disabled = true;

    try {
        const response = await fetch('/api/agent/chat', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ message: userMessage })
        });
        const data = await response.json();
        if (response.ok) {
            appendAgentMessage(data.responseMessage, false);
            updateAgentSidePanel(data);
        } else {
            appendAgentMessage('⚠️ Error processing query: ' + (data.error || 'Please try again.'), false);
        }
    } catch (error) {
        appendAgentMessage('⚠️ Network error connecting to Weather AI Agent.', false);
    } finally {
        if (sendBtn) sendBtn.disabled = false;
    }
}

function sendQuickPrompt(text) {
    const input = document.getElementById('agentInput');
    if (input) {
        input.value = text;
        handleAgentSubmit();
    }
}

function appendAgentMessage(text, isUser) {
    const stream = document.getElementById('chatStream');
    if (!stream) return;

    const messageDiv = document.createElement('div');
    messageDiv.className = `message-bubble ${isUser ? 'user-message' : 'agent-message'}`;

    if (!isUser) {
        const header = `<div class="d-flex align-items-center mb-1"><i class="bi bi-robot text-primary me-2 fs-5"></i><strong class="text-primary">Weather AI Agent</strong></div>`;
        messageDiv.innerHTML = header + formatMarkdownText(text);
    } else {
        messageDiv.textContent = text;
    }

    stream.appendChild(messageDiv);
    stream.scrollTop = stream.scrollHeight;
}

function formatMarkdownText(text) {
    if (!text) return '';
    return text
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/\n/g, '<br/>');
}

function updateAgentSidePanel(data) {
    if (!data) return;

    // 1. Decision Tree Badge
    const dtBadge = document.getElementById('dtBadge');
    const dtIcon = document.getElementById('dtIcon');
    if (dtBadge) {
        dtBadge.textContent = data.decisionTreePrediction || 'No Rain';
        if ((data.decisionTreePrediction || '').toLowerCase().includes('rain')) {
            dtBadge.className = 'badge bg-danger badge-prediction shadow-sm';
            if (dtIcon) dtIcon.className = 'bi bi-cloud-rain-fill text-primary fs-1';
        } else {
            dtBadge.className = 'badge bg-success badge-prediction shadow-sm';
            if (dtIcon) dtIcon.className = 'bi bi-sun-fill text-warning fs-1';
        }
    }

    // 2. Regression Temp
    const regValue = document.getElementById('regValue');
    if (regValue && data.regressionPrediction !== null && data.regressionPrediction !== undefined) {
        regValue.textContent = data.regressionPrediction.toFixed(1);
    }

    // 3. Confidence Bar
    const confPercent = document.getElementById('confidencePercent');
    const confBar = document.getElementById('confidenceBar');
    if (data.confidenceScore) {
        const pct = Math.round(data.confidenceScore * 100);
        if (confPercent) confPercent.textContent = pct + '%';
        if (confBar) confBar.style.width = pct + '%';
    }

    // 4. Recommendations List
    const recList = document.getElementById('recommendationsList');
    if (recList && data.recommendations && data.recommendations.length > 0) {
        recList.innerHTML = '';
        data.recommendations.forEach(rec => {
            const li = document.createElement('li');
            li.className = 'list-group-item px-0 text-dark small fw-medium d-flex align-items-start gap-2 border-bottom-0';
            li.innerHTML = `<span>${rec}</span>`;
            recList.appendChild(li);
        });
    }
}

async function loadAgentInitialInsights() {
    if (document.body.dataset.page !== 'agent') return;
    try {
        const response = await fetch('/api/agent/insights');
        if (!response.ok) return;
        const data = await response.json();
        updateAgentSidePanel(data);
    } catch (error) {
        console.error(error);
    }
}

async function loadSchedulerStatus() {
    try {
        const response = await fetch('/api/scheduler/status');
        if (!response.ok) return;
        const data = await response.json();
        updateSchedulerUI(data);
    } catch (error) {
        console.error(error);
    }
}

async function triggerManualRetrain() {
    try {
        const response = await fetch('/api/scheduler/trigger', { method: 'POST' });
        if (!response.ok) return;
        const data = await response.json();
        updateSchedulerUI(data);
        if (typeof loadModelPerformance === 'function') {
            loadModelPerformance();
        }
    } catch (error) {
        console.error(error);
    }
}

function updateSchedulerUI(data) {
    const statusEl = document.getElementById('schedStatus');
    const lastRunEl = document.getElementById('schedLastRun');
    const totalRunsEl = document.getElementById('schedTotalRuns');
    const msgEl = document.getElementById('schedMessage');

    if (statusEl) statusEl.textContent = data.lastStatus || 'Active';
    if (lastRunEl) lastRunEl.textContent = data.lastExecutionTime || 'N/A';
    if (totalRunsEl) totalRunsEl.textContent = data.totalRuns !== undefined ? data.totalRuns : '0';
    if (msgEl) msgEl.textContent = data.lastMessage || 'Background scheduler active.';
}

function initPage() {
    const page = document.body.dataset.page;
    if (page === 'weather') {
        loadWeatherRecords();
    }
    if (page === 'history') {
        loadPredictionHistory();
    }
    if (page === 'performance' || page === 'dashboard') {
        loadModelPerformance();
        loadSchedulerStatus();
    }
    if (page === 'dashboard') {
        loadDashboard();
    }
    if (page === 'agent') {
        loadAgentInitialInsights();
    }
}

window.addEventListener('DOMContentLoaded', initPage);


