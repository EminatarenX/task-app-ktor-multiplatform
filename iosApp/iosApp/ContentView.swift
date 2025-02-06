import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let controller = MainViewControllerKt.MainViewController()
        controller.view.backgroundColor = UIColor(red: 0.118, green: 0.106, blue: 0.141, alpha: 1.0)
        return controller
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.keyboard) // Volvemos a .keyboard en lugar de .all
            .background(Color(UIColor(red: 0.118, green: 0.106, blue: 0.141, alpha: 1.0)))
    }
}

#Preview {
    ContentView()
}
