#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint hyper_lpr.podspec' to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'hyper_lpr'
  s.version          = '0.0.1'
  s.summary          = 'A new Flutter plugin.'
  s.description      = <<-DESC
A new Flutter plugin.
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.platform = :ios, '9.0'

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.static_framework = true
  # prefix_header_file
  s.frameworks = 'UIKit','Foundation', 'AudioToolbox', 'AVFoundation', 'CoreVideo', 'AssetsLibrary', 'CoreMedia'
  s.dependency 'OpenCV'
  s.dependency 'Masonry'
  s.libraries = 'c++', 'stdc++'
  # s.xcconfig = {
  #      'CLANG_CXX_LANGUAGE_STANDARD' => 'c++11',
  #      'CLANG_CXX_LIBRARY' => 'libc++'
  # }
end
