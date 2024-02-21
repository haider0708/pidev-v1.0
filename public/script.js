// Side Menu
const allSideMenu = document.querySelectorAll('#sidebar .side-menu.top li a');

allSideMenu.forEach(item => {
	const li = item.parentElement;

	item.addEventListener('click', function () {
		allSideMenu.forEach(i => {
			i.parentElement.classList.remove('active');
		})
		li.classList.add('active');
	})
});



// Search Button
const searchButton = document.querySelector('#content nav form .form-input button');
const searchButtonIcon = document.querySelector('#content nav form .form-input button .bx');
const searchForm = document.querySelector('#content nav form');

if (searchButton) {
	searchButton.addEventListener('click', function (e) {
		if (window.innerWidth < 576) {
			e.preventDefault();
			searchForm.classList.toggle('show');
			if (searchForm.classList.contains('show')) {
				searchButtonIcon.classList.replace('bx-search', 'bx-x');
			} else {
				searchButtonIcon.classList.replace('bx-x', 'bx-search');
			}
		}
	});
}

// Initial Setup
if (window.innerWidth < 768) {
	sidebar.classList.add('hide');
} else if (window.innerWidth > 576) {
	if (searchButtonIcon) {
		searchButtonIcon.classList.replace('bx-x', 'bx-search');
	}
	if (searchForm) {
		searchForm.classList.remove('show');
	}
}

// Window Resize Event
window.addEventListener('resize', function () {
	if (this.innerWidth > 576) {
		if (searchButtonIcon) {
			searchButtonIcon.classList.replace('bx-x', 'bx-search');
		}
		if (searchForm) {
			searchForm.classList.remove('show');
		}
	}
});

// Switch Mode
const switchMode = document.getElementById('switch-mode');

if (switchMode) {
	switchMode.addEventListener('change', function () {
		if (this.checked) {
			document.body.classList.add('dark');
		} else {
			document.body.classList.remove('dark');
		}
	});
}
