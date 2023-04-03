import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';

function AppNavbar() {
    return (
        <>
            <Navbar bg="light" variant="light" className={"mb-5"}>
                <Container>
                    <Navbar.Brand>Quote collector</Navbar.Brand>
                </Container>
            </Navbar>
        </>
    );
}

export default AppNavbar;