//import
use std::cell::Cell;

//enumeration
enum LogMessage {
    HelloWorld,
    AString(String),
    ANewUser(String, u32),
}

//interface
trait Logger {
    //only (immutably) borrowing object, "self" would own it, 
    //"&mut self" would mutably borrow it
    fn log(&self, message: LogMessage);
}

//data
struct LogPrinter {
    //Generic type - allow interior mutability, only the place that owns the
    //cell can free/destroy the cell
    message_id: Cell<u32>
    last_user_id: Cell<u32>
}

//implementation
impl LogPrinter {
    fn new() -> LogPrinter {
        LogPrinter {
            message_id: Cell::new(0)
        }
    }
}

//making data and implementation obey trait
impl Logger for LogPrinter {
    fn log(&self, message: LogMessage) {
        //retrieve id as an int from LogPrinter
        let message_id = self.message_id.get();
        //println!() a macro, not a method
        match message {
            //can match on enums, or actual values, and have defaults
            //if match uses owned value, ownership moved,
            //if borrowed value, ownership remains, but...
            LogMessage::HelloWorld =>
                println!("{}: Hello World!", message_id),
            LogMessage::AString(string) =>
                println!("{}: {}", message_id, string),
            LogMessage::ANewUser(user_name, user_id) => {
                println!("{}: {}", message_id, user_name);
                self.user_id.set(user_id);
            },
        }
        //return id to LogPrinter
        self.message_id.set(message_id + 1);
    }
}

fn main() {
    //logger:LogPrinter - Type inferance
    let logger = LogPrinter::new();
    logger.log(LogMessage::HelloWorld);
    //"Rust = C + ML" is a statically allocated immutable string
    logger.log(LogMessage::AString("Rust = C + ML".into()));
    logger.log(LogMessage::ANewUser("Oliver".into(), ));
}
