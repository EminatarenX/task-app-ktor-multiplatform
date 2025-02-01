import UIKit
import ComposeApp

class MainViewController: UIViewController {
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor(red: 0.118, green: 0.106, blue: 0.141, alpha: 1.0)
    }
}
