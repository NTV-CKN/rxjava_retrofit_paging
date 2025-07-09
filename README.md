- App basic, sử dụng retrofit kéo dữ liệu từ API:
  + https://api.github.com/users/{username}/repos
  + https://api.github.com/users/{username}
- Sử dụng thư viện rxjava thao tác bất đồng bộ, paging phân trang kéo dữ liệu từ repos của 1 user được chỉ định (Chỉ xử lí load lũy tiến)
- Sơ lược cách hoạt động:
  + User sử dụng thanh tìm kiếm và nhập tên user name, nếu có thì hiện item của user đó. Ngược lại thông báo not found
  + Khi user ấn vào item user hiện khi tìm kiếm sẽ chạy 1 fragment và hiển thị các repositories của user đó
