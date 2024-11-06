// editUserFields.js

// 필드 수정 버튼 클릭 시 호출되는 함수
function editField(field) {
    const fieldValue = document.getElementById(field).innerText;
    const input = `<input type="text" id="${field}-input" value="${fieldValue}" class="form-control" style="display:inline-block; width:auto;"/>`;
    document.getElementById(field).innerHTML = input;

    // 기존 edit 버튼 숨기기
    const editButton = document.querySelector(`button[onclick="editField('${field}')"]`);
    if (editButton) editButton.style.display = 'none';

    // save 버튼 추가
    const saveButton = `<button class="btn btn-success btn-sm ms-2" onclick="saveField('${field}')">Save</button>`;
    document.getElementById(field).insertAdjacentHTML('afterend', saveButton);
}

// 필드 저장 버튼 클릭 시 호출되는 함수
function saveField(field) {
    const inputValue = document.getElementById(`${field}-input`).value;

    // AJAX 요청 보내기
    $.ajax({
        url: '/users/mypage/edit',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ [field]: inputValue }),
        success: function(response) {
            document.getElementById(field).innerText = inputValue;
            removeSaveButton(field);
            showModal("수정되었습니다.");
        },
        error: function(xhr, status, error) {
            showModal("수정에 실패했습니다.");
        }
    });
}

// 저장 버튼 제거 및 edit 버튼 표시
function removeSaveButton(field) {
    const saveButton = document.querySelector(`button[onclick="saveField('${field}')"]`);
    if (saveButton) saveButton.remove();

    // edit 버튼 다시 표시
    const editButton = document.querySelector(`button[onclick="editField('${field}')"]`);
    if (editButton) editButton.style.display = 'inline-block';
}

// 모달 표시 함수
function showModal(message) {
    document.querySelector("#feedbackModal .modal-body").innerText = message;
    document.getElementById("feedbackModal").style.display = "block";
}

// 모달 닫기 함수
function closeModal() {
    document.getElementById("feedbackModal").style.display = "none";
}
