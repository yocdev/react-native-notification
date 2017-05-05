
Pod::Spec.new do |s|
  s.name         = "RNPush"
  s.version      = "1.0.0"
  s.summary      = "RNPush"
  s.description  = <<-DESC
                  RNPush
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/author/RNPush.git", :tag => "master" }
  s.source_files  = "RNPush/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  