SystemUtil.Run "C:\Program Files\Notepad++\notepad++.exe"
Window("Notepad++").WinObject("Scintilla").Type "Test" @@ hightlight id_;_199794_;_script infofile_;_ZIP::ssf5.xml_;_
Window("Notepad++").WinObject("Scintilla").Type micCtrlDwn + "s" + micCtrlUp @@ hightlight id_;_199794_;_script infofile_;_ZIP::ssf6.xml_;_
Window("Notepad++").Dialog("Save As").WinEdit("File name:").Set "Test2.txt" @@ hightlight id_;_2165100_;_script infofile_;_ZIP::ssf7.xml_;_
Window("Notepad++").WinTab("SysTabControl32").Select "Test2.txt" @@ hightlight id_;_199786_;_script infofile_;_ZIP::ssf10.xml_;_
Window("Notepad++").Close @@ hightlight id_;_65976_;_script infofile_;_ZIP::ssf11.xml_;_
